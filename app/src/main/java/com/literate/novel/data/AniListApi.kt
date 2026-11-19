package com.literate.novel.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class AniListPageResult(
    val novels: List<Novel>,
    val hasNextPage: Boolean,
    val totalCount: Int
)

object AniListApi {
    private const val ENDPOINT = "https://graphql.anilist.co"

    private const val QUERY = """
        query (${'$'}page: Int, ${'$'}perPage: Int, ${'$'}search: String, ${'$'}genre: String, ${'$'}tag: String, ${'$'}status: MediaStatus) {
          Page(page: ${'$'}page, perPage: ${'$'}perPage) {
            pageInfo {
              total
              hasNextPage
            }
            media(
              type: MANGA
              format: NOVEL
              sort: POPULARITY_DESC
              search: ${'$'}search
              genre: ${'$'}genre
              tag: ${'$'}tag
              status: ${'$'}status
            ) {
              id
              title {
                english
                romaji
              }
              description(asHtml: false)
              status
              chapters
              volumes
              averageScore
              popularity
              countryOfOrigin
              startDate {
                year
              }
              genres
              coverImage {
                large
              }
              staff(perPage: 1) {
                nodes {
                  name {
                    full
                  }
                }
              }
            }
          }
        }
    """

    suspend fun fetchNovels(
        page: Int = 1,
        perPage: Int = 20,
        search: String? = null,
        genre: String? = null,
        status: String? = null
    ): AniListPageResult = withContext(Dispatchers.IO) {
        val variables = JSONObject().apply {
            put("page", page)
            put("perPage", perPage)
            if (!search.isNullOrBlank()) {
                put("search", search.trim())
            }
            if (!genre.isNullOrBlank() && genre != "Semua") {
                when (genre) {
                    "Xianxia" -> put("tag", "Cultivation")
                    "Dark" -> put("genre", "Psychological")
                    else -> put("genre", genre)
                }
            }
            when (status) {
                "Tamat" -> put("status", "FINISHED")
                "Ongoing" -> put("status", "RELEASING")
            }
        }

        val body = JSONObject().apply {
            put("query", QUERY)
            put("variables", variables)
        }.toString()

        val conn = (URL(ENDPOINT).openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = 7000
            readTimeout = 7000
            doOutput = true
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            setRequestProperty("User-Agent", "LiterateAndroid/1.0")
        }

        try {
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val text = stream?.bufferedReader()?.use { it.readText() } ?: ""
            if (code !in 200..299 || text.isBlank()) {
                return@withContext AniListPageResult(emptyList(), false, 0)
            }

            val root = JSONObject(text)
            val pageObj = root.optJSONObject("data")?.optJSONObject("Page")
                ?: return@withContext AniListPageResult(emptyList(), false, 0)
            val pageInfo = pageObj.optJSONObject("pageInfo")
            val total = pageInfo?.optInt("total", 0) ?: 0
            val hasNext = pageInfo?.optBoolean("hasNextPage", false) ?: false
            val mediaArr = pageObj.optJSONArray("media") ?: JSONArray()

            val list = ArrayList<Novel>(mediaArr.length())
            for (i in 0 until mediaArr.length()) {
                val m = mediaArr.optJSONObject(i) ?: continue
                val id = "al_${m.optInt("id")}"
                val titleObj = m.optJSONObject("title")
                val english = titleObj?.optString("english", "")?.takeIf { it.isNotBlank() && it != "null" }
                val romaji = titleObj?.optString("romaji", "")?.takeIf { it.isNotBlank() && it != "null" }
                val title = english ?: romaji ?: continue

                val rawDesc = m.optString("description", "")
                val cleanDesc = cleanSynopsis(rawDesc).ifBlank {
                    "Novel populer dari katalog global LITERATE. Tambahkan ke perpustakaan dan berikan ulasanmu!"
                }

                val statusStr = when (m.optString("status", "")) {
                    "FINISHED" -> "Tamat"
                    else -> "Ongoing"
                }

                val chapters = m.optInt("chapters", 0).let { ch ->
                    if (ch > 0) ch else (m.optInt("volumes", 0) * 12).coerceAtLeast(24)
                }

                val avgScore = m.optInt("averageScore", 0)
                val rating = if (avgScore > 0) {
                    val scaled = (avgScore / 20.0).coerceIn(3.5, 5.0)
                    (kotlin.math.round(scaled * 10.0) / 10.0)
                } else 4.5

                val votes = (m.optInt("popularity", 500) / 10).coerceAtLeast(120)

                val lang = when (m.optString("countryOfOrigin", "")) {
                    "CN" -> "Chinese (Mandarin)"
                    "KR" -> "Korean"
                    "JP" -> "Japanese"
                    else -> "English"
                }

                val year = m.optJSONObject("startDate")?.optInt("year", 2020)?.takeIf { it > 0 } ?: 2020

                val genresArr = m.optJSONArray("genres")
                val genres = mutableListOf<String>()
                if (genresArr != null) {
                    for (g in 0 until genresArr.length()) {
                        val gv = genresArr.optString(g, "")
                        if (gv.isNotBlank()) genres.add(gv)
                    }
                }
                if (genres.isEmpty()) genres.add("Fantasy")

                val coverUrl = m.optJSONObject("coverImage")?.optString("large", "") ?: ""

                val author = m.optJSONObject("staff")
                    ?.optJSONArray("nodes")
                    ?.optJSONObject(0)
                    ?.optJSONObject("name")
                    ?.optString("full", "")
                    ?.takeIf { it.isNotBlank() && it != "null" }
                    ?: "Unknown Author"

                list.add(
                    Novel(
                        id = id,
                        title = title,
                        coverUrl = coverUrl,
                        rating = rating,
                        totalVotes = votes,
                        status = statusStr,
                        chapters = chapters,
                        genres = genres,
                        author = author,
                        originalLanguage = lang,
                        releaseYear = year,
                        synopsis = cleanDesc,
                        chaptersList = listOf(
                            ChapterItem(1, "Chapter 1: Prologue", "1.4k reads", "8 min read"),
                            ChapterItem(2, "Chapter 2: The Beginning", "1.1k reads", "9 min read"),
                            ChapterItem(3, "Chapter 3: Turning Point", "950 reads", "8 min read")
                        ),
                        reviews = listOf(
                            ReviewItem(
                                id = "rev_${id}_1",
                                userName = "PembacaLiterate",
                                avatarColor = 0xFF6366F1,
                                stars = 4.5f,
                                content = "Ceritanya menarik dan bikin penasaran di setiap babnya. Sangat direkomendasikan!"
                            )
                        )
                    )
                )
            }

            AniListPageResult(list, hasNext, total)
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: Exception) {
            AniListPageResult(emptyList(), false, 0)
        } finally {
            conn.disconnect()
        }
    }

    private fun cleanSynopsis(raw: String): String {
        if (raw.isBlank() || raw == "null") return ""
        return raw
            .replace(Regex("<br\\s*/?>", RegexOption.IGNORE_CASE), "\n")
            .replace(Regex("<[^>]*>"), "")
            .replace("&quot;", "\"")
            .replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&#39;", "'")
            .replace(Regex("\\n{3,}"), "\n\n")
            .trim()
    }
}

