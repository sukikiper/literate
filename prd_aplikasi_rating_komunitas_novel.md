# Product Requirement Document (PRD)
## Aplikasi Rating & Komunitas Novel (Namatentatif: "BookCircle" / "NovelSphere")

---

## 1. Ringkasan Produk & Visi

### 1.1 Visi Produk
Menjadi platform katalog, rating, dan komunitas novel terbesar dan terpercaya yang menghubungkan para pembaca, pengulas, dan pencinta novel. Platform ini berfokus murni pada **penemuan (*discovery*), ulasan mendalam, pelacakan bacaan (*reading tracking*), dan diskusi komunitas**, serta **TIDAK menyediakan layanan membaca novel/teks penuh**.

### 1.2 Tujuan Utama
1. Menyediakan database novel yang lengkap dengan data meta terverifikasi dan gambar kover beresolusi tinggi.
2. Memfasilitasi interaksi sosial antar pembaca melalui ulasan, rating, rak buku digital (*bookshelves*), dan forum diskusi.
3. Memberikan rekomendasi novel yang dipersonalisasi berdasarkan preferensi bacaan dan riwayat ulasan pengguna.

### 1.3 Batasan Produk (*Out of Scope*)
*   **TIDAK** menyediakan pembaca e-book (*e-reader*) atau konten teks lengkap bab per bab dari novel.
*   **TIDAK** memproses transaksi penjualan buku fisik maupun digital secara langsung (hanya menyediakan tautan ke penjual resmi/publisher).

---

## 2. Pengguna Sasaran (*Target Audience*)

1. **Casual Readers:** Pembaca yang ingin mencatat buku yang sudah/sedang dibaca dan melihat rating sebelum membeli buku.
2. **Avid Readers & Reviewers:** Pembaca aktif yang suka menulis ulasan kritis, memberikan *star rating*, dan membagikan rekomendasi.
3. **Komunitas / Book Clubs:** Kelompok pembaca yang ingin berdiskusi per bab, per tema, atau mengadakan tantangan membaca (*reading challenge*).
4. **Penulis & Penerbit (Verified Account):** Pihak yang ingin mengklaim profil buku mereka, mengunggah data kover resmi, dan berinteraksi dengan pembaca.

---

## 3. Spesifikasi Data Novel (*Comprehensive Data Schema*)

Agar database novel lengkap dan kaya informasi, setiap entitas novel wajib memiliki struktur data sebagai berikut:

| Kategori Data | Field Name | Tipe Data | Keterangan / Ketentuan |
| :--- | :--- | :--- | :--- |
| **Media Utama** | `front_cover_url` | String (URL) | Gambar kover depan (High-res min. 1080x1620 px, aspek rasio 2:3). |
| | `back_cover_url` | String (URL) | Gambar kover belakang (Opsional). |
| | `additional_media` | Array[URL] | Foto edisi spesial/preview ilustrasi dalam buku (opsional). |
| **Identitas Dasar** | `title` | String | Judul novel (Bahasa asli & judul terjemahan). |
| | `original_title` | String | Judul asli jika karya terjemahan. |
| | `authors` | Array[AuthorID] | Penulis (bisa lebih dari satu). |
| | `translators` | Array[String] | Penerjemah (jika ada). |
| | `illustrators` | Array[String] | Ilustrator kover/isi (jika ada). |
| | `publisher` | String | Nama penerbit. |
| | `publish_date` | Date | Tanggal terbit resmi. |
| | `edition_type` | Enum | Hardcover, Paperback, Audiobook, Web Novel (Metadata only). |
| | `isbn_10` / `isbn_13` | String | Nomor ISBN unik untuk verifikasi. |
| | `language` | String | Bahasa terbitan. |
| | `page_count` | Integer | Jumlah halaman buku. |
| **Detail Konten** | `synopsis` | Text | Sinopsis resmi dari penerbit/penulis. |
| | `genres` | Array[Tag] | Fantasi, Romance, Thriller, Sci-Fi, Slice of Life, dll. |
| | `sub_genres` | Array[Tag] | LitRPG, Cultivation, Historical Romance, dll. |
| | `tropes` | Array[Tag] | Enemies-to-Lovers, Found Family, Slow Burn, dll. |
| | `content_warnings` | Array[Tag] | Peringatan konten (Kekerasan, Isu Sensitif, dll.). |
| | `series_info` | Object | Nama seri, nomor urut buku dalam seri (contoh: *Bumi Series #3*). |
| **Agregat Rating** | `average_rating` | Float | Rata-rata skor rating (0.0 - 5.0). |
| | `total_ratings` | Integer | Jumlah total pengguna yang memberi rating. |
| | `total_reviews` | Integer | Jumlah ulasan tertulis. |
| | `rating_breakdown` | Object | Jumlah vote untuk 1-5 bintang. |
| **Tautan Luar** | `purchase_links` | Array[Object] | Tautan pembelian resmi (Gramedia, Tokopedia, Shopee, Amazon, Kobo, dll.). |

---

## 4. Fitur Utama (*Core Features*)

### 4.1 Modul Katalog & Penemuan (*Discovery*)
1. **Search Engine & Advanced Filter:**
   *   Pencarian berdasarkan Judul, Penulis, ISBN, Genre, Trope, dan Kata Kunci.
   *   Filter berdasarkan: Rating rata-rata, Tahun terbit, Jumlah halaman, Bahasa, dan Kategori Peringatan Konten.
2. **Kategori & Trending Feed:**
   *   *Top Rated of the Month*, *Most Reviewed*, *New Releases*, dan *Trending in Community*.
3. **Halaman Detail Novel:**
   *   Tampilan visual kover besar dengan efek jilid/buku 3D halus.
   *   Informasi metadata lengkap, sinopsis (dengan tombol *read more*).
   *   Daftar buku dalam seri yang sama (*In this series*).
   *   Buku serupa (*Readers also enjoyed*).

### 4.2 Engine Rating & Ulasan (*Rating & Review System*)
1. **Sistem Rating Kustom:**
   *   Skala 5 Bintang dengan presisi setengah bintang ($0.5$ hingga $5.0$).
   *   Rating aspek opsional: *Pacing*, *Character Development*, *World Building*, *Plot Twist*.
2. **Penulisan Ulasan:**
   *   Rich Text Editor (Bold, Italic, Bullet points).
   *   **Spoiler Protection Tag:** Kemampuan menandai paragraf/seluruh ulasan sebagai *spoiler* (teks disembunyikan sampai di-klik oleh pengguna).
   *   **Progress Tagging:** Menandai progress bacaan saat membuat ulasan (misal: "Ulasan pada Halaman 120" atau "Ulasan Bab 15").
3. **Interaksi Ulasan:**
   *   Tanggapan *Like*, *Helpful*, *Agree*, *Disagree*.
   *   Komentar pada ulasan pengguna lain.

### 4.3 Fitur Komunitas & Sosial (*Community Engine*)
1. **Rak Buku Digital (*Custom Bookshelves*):**
   *   Rak default: *Want to Read*, *Currently Reading*, *Read*, *DNF (Did Not Finish)*.
   *   Rak Kustom buatan pengguna: Misal "*Favorite 2026*", "*Bikin Nangis*", "*Re-read Worthy*".
2. **Klub Buku & Forum Diskusi (*Book Clubs & Forums*):**
   *   Pengguna dapat membuat atau bergabung dengan Klub Buku Publik/Privat.
   *   Utas Diskusi (*Discussion Threads*): Diskusi bebas, Diskusi spoiler bab tertentu, *Read-along event*.
3. **Fitur Sosial & Aktivitas Feed:**
   *   *Follow/Unfollow* sesama pengguna dan Penulis.
   *   *Activity Feed*: Melihat novel apa yang sedang dibaca, diulas, atau diberi rating oleh teman.
4. **Reading Challenges & Goal Tracking:**
   *   Tantangan membaca tahunan (contoh: "Target 50 Buku di 2026").
   *   Statistik bacaan pengguna (Grafik genre favorit, total halaman dibaca, distribusi rating).

### 4.4 Modul Kontribusi Data (*Crowdsourced & Verified Metadata*)
1. **Pengajuan Data Novel Baru:**
   *   Formulir bagi pengguna untuk mengajukan novel yang belum ada di database (wajib mengunggah kover depan resolusi tinggi, sinopsis, dan ISBN/informasi valid).
2. **Modur Moderator/Admin:**
   *   Tim kurasi untuk memverifikasi dan menyetujui submission data/kover baru sebelum dipublikasikan demi menjaga kelengkapan dan kualitas data.

---

## 5. Kebutuhan Non-Fungsional (*Non-Functional Requirements*)

1. **Performa Tampilan Gambar (Media Optimization):**
   *   Semua gambar kover dikompresi ke format Next-Gen (WebP/AVIF) dengan CDN (*Content Delivery Network*) untuk memastikan *loading* kover instan di aplikasi.
2. **Keamanan & Moderasi Konten:**
   *   Sistem proteksi terhadap *review bombing* atau spam ulasan palsu.
   *   Pelaporan konten (*Report System*) untuk ulasan bermuatan kebencian atau spoiler tanpa tag.
3. **Responsivitas UI/UX:**
   *   Desain *Mobile-First* yang bersih, berfokus pada kejelasan tipografi dan keindahan kover buku.

---

## 6. Metrik Keberhasilan (*Key Performance Indicators - KPIs*)

*   **DAU/MAU (Daily/Monthly Active Users):** Tingkat keaktifan pengguna dalam membuka aplikasi.
*   **User Engagement:** Jumlah ulasan tertulis dan rating yang diberikan per minggu.
*   **Database Growth & Quality:** Persentase novel populer yang memiliki data kover *high-res* dan metadata 100% lengkap.
*   **Retention Rate:** Persentase pengguna yang kembali mencatat buku dalam 30 hari.

---

## 7. Rencana Pengembangan (*Phasing / Roadmap*)

*   **Fase 1 (MVP):** Auth Pengguna, Database Novel (Katalog + Kover), Rak Buku Dasar, Sistem Rating & Ulasan dasar (dengan Spoiler Tag), Profil Pengguna.
*   **Fase 2 (Komunitas & Sosial):** Activity Feed, Custom Bookshelves, System Follow, Reading Challenge Tahunan.
*   **Fase 3 (Klub Buku & Gamifikasi):** Forum/Klub Buku, Badge Pengulas, Statistik Kompleks, Rekomendasi AI berdasarkan riwayat bacaan.