package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*

class QuranRepository {

    fun getSurahList(): List<Surah> {
        return listOf(
            Surah(1, "الفَاتِحَة", "Al-Fatihah", "আল-ফাতিহা", "The Opening", "সূচনা", 7, "Meccan", 1),
            Surah(2, "البَقَرَة", "Al-Baqarah", "আল-বাকারা", "The Cow", "গাভী", 286, "Medinan", 1),
            Surah(3, "آلِ عِمْرَان", "Ali 'Imran", "আলে ইমরান", "Family of Imran", "ইমরানের পরিবার", 200, "Medinan", 3),
            Surah(4, "النِّسَاء", "An-Nisa", "আন-নিসা", "The Women", "নারী", 176, "Medinan", 4),
            Surah(5, "المَائِدَة", "Al-Ma'idah", "আল-মায়েদা", "The Table Spread", "খাদ্য পরিবেশিত টেবিল", 120, "Medinan", 6),
            Surah(6, "الأَنْعَام", "Al-An'am", "আল-আনআম", "The Cattle", "গৃহপালিত পশু", 165, "Meccan", 7),
            Surah(7, "الأَعْرَاف", "Al-A'raf", "আল-আরাফ", "The Heights", "উঁচু স্থানসমূহ", 206, "Meccan", 8),
            Surah(8, "الأَنْفَال", "Al-Anfal", "আল-আনফাল", "The Spoils of War", "যুদ্ধলব্ধ ধন-সম্পদ", 75, "Medinan", 9),
            Surah(9, "التَّوْبَة", "At-Tawbah", "আত-তাওবাহ", "The Repentance", "অনুশোচনা", 129, "Medinan", 10),
            Surah(10, "يُونُس", "Yunus", "ইউনুস", "Jonah", "ইউনুস (আঃ)", 109, "Meccan", 11),
            Surah(18, "الكَهْف", "Al-Kahf", "আল-কাহফ", "The Cave", "গুহা", 110, "Meccan", 15),
            Surah(36, "يس", "Yaseen", "ইয়াসীন", "Ya-Sin", "ইয়াসীন", 83, "Meccan", 22),
            Surah(55, "الرَّحْمَن", "Ar-Rahman", "আর-রহমান", "The Beneficent", "পরম দয়ালু", 78, "Medinan", 27),
            Surah(67, "المُلْك", "Al-Mulk", "আল-মুলক", "The Sovereignty", "রাজত্ব", 30, "Meccan", 29),
            Surah(112, "الإِخْلَاص", "Al-Ikhlas", "আল-ইখলাস", "The Sincerity", "একনিষ্ঠতা", 4, "Meccan", 30),
            Surah(113, "الفَلَق", "Al-Falaq", "আল-ফালাক", "The Daybreak", "ভোরবেলা", 5, "Meccan", 30),
            Surah(114, "النَّاس", "An-Nas", "আন-নাস", "Mankind", "মানুষ", 6, "Meccan", 30)
        )
    }

    fun getAyahsForSurah(surahNumber: Int): List<Ayah> {
        return when (surahNumber) {
            1 -> listOf(
                Ayah(
                    id = 1,
                    surahNumber = 1,
                    ayahNumber = 1,
                    textArabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    textEnglish = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                    textBangla = "পরম করুণাময় অসীম দয়ালু আল্লাহর নামে শুরু করছি।",
                    tafsirShort = "This verse signifies beginning all actions with Allah's name seeking blessing and mercy.",
                    words = listOf(
                        WordDetail(1, "بِسْمِ", "Bismi", "In the name", "নামে"),
                        WordDetail(2, "اللَّهِ", "Allah", "of Allah", "আল্লাহর"),
                        WordDetail(3, "الرَّحْمَٰنِ", "Ar-Rahman", "the Most Gracious", "পরম করুণাময়"),
                        WordDetail(4, "الرَّحِيمِ", "Ar-Raheem", "the Most Merciful", "অসীম দয়ালু")
                    )
                ),
                Ayah(
                    id = 2,
                    surahNumber = 1,
                    ayahNumber = 2,
                    textArabic = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ",
                    textEnglish = "[All] praise is [due] to Allah, Lord of the worlds -",
                    textBangla = "সকল প্রশংসা জগতসমূহের প্রতিপালক আল্লাহর জন্য।",
                    tafsirShort = "Praise belongs strictly to Allah as the sole Creator and Sustainer of every existing creation.",
                    words = listOf(
                        WordDetail(1, "الْحَمْدُ", "Al-hamdu", "All praise", "সকল প্রশংসা"),
                        WordDetail(2, "لِلَّهِ", "Lillahi", "is for Allah", "আল্লাহর জন্য"),
                        WordDetail(3, "رَبِّ", "Rabbi", "Lord", "প্রতিপালক"),
                        WordDetail(4, "الْعَالَمِينَ", "Al-'alameen", "of the worlds", "জগতসমূহের")
                    )
                ),
                Ayah(
                    id = 3,
                    surahNumber = 1,
                    ayahNumber = 3,
                    textArabic = "الرَّحْمَٰنِ الرَّحِيمِ",
                    textEnglish = "The Entirely Merciful, the Especially Merciful,",
                    textBangla = "যিনি পরম করুণাময় ও পরম দয়ালু।",
                    tafsirShort = "Reiteration of Allah's divine attributes of mercy to show kindness over punishment.",
                    words = listOf(
                        WordDetail(1, "الرَّحْمَٰنِ", "Ar-Rahman", "The Most Gracious", "পরম করুণাময়"),
                        WordDetail(2, "الرَّحِيمِ", "Ar-Raheem", "The Most Merciful", "পরম দয়ালু")
                    )
                ),
                Ayah(
                    id = 4,
                    surahNumber = 1,
                    ayahNumber = 4,
                    textArabic = "مَالِكِ يَوْمِ الدِّينِ",
                    textEnglish = "Sovereign of the Day of Recompense.",
                    textBangla = "বিচার দিনের মালিক।",
                    tafsirShort = "Owner and Master of the Day of Judgment when humanity will account for actions.",
                    words = listOf(
                        WordDetail(1, "مَالِكِ", "Maliki", "Owner", "মালিক"),
                        WordDetail(2, "يَوْمِ", "Yawmi", "of the Day", "দিনের"),
                        WordDetail(3, "الدِّينِ", "Ad-Deen", "of Judgment", "বিচারের")
                    )
                ),
                Ayah(
                    id = 5,
                    surahNumber = 1,
                    ayahNumber = 5,
                    textArabic = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                    textEnglish = "It is You we worship and You we ask for help.",
                    textBangla = "আমরা কেবল তোমারই ইবাদত করি এবং কেবল তোমারই সাহায্য প্রার্থনা করি।",
                    tafsirShort = "The core of Islamic Monotheism (Tawhid): Worship is exclusive to Allah alone.",
                    words = listOf(
                        WordDetail(1, "إِيَّاكَ", "Iyyaka", "You alone", "তোমাকেই"),
                        WordDetail(2, "نَعْبُدُ", "Na'budu", "we worship", "আমরা ইবাদত করি"),
                        WordDetail(3, "وَإِيَّاكَ", "Wa-iyyaka", "and You alone", "এবং তোমাকেই"),
                        WordDetail(4, "نَسْتَعِينُ", "Nasta'een", "we ask for help", "সাহায্য চাই")
                    )
                ),
                Ayah(
                    id = 6,
                    surahNumber = 1,
                    ayahNumber = 6,
                    textArabic = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                    textEnglish = "Guide us to the straight path -",
                    textBangla = "আমাদের সহজ-সঠিক পথ প্রদর্শন করো।",
                    tafsirShort = "A fundamental plea for continuous divine guidance upon the true religion.",
                    words = listOf(
                        WordDetail(1, "اهْدِنَا", "Ihdina", "Guide us", "আমাদের পরিচালিত কর"),
                        WordDetail(2, "الصِّرَاطَ", "As-Sirat", "the path", "পথের দিকে"),
                        WordDetail(3, "الْمُسْتَقِيمَ", "Al-Mustaqeem", "the straight", "সরল-সঠিক")
                    )
                ),
                Ayah(
                    id = 7,
                    surahNumber = 1,
                    ayahNumber = 7,
                    textArabic = "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ",
                    textEnglish = "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.",
                    textBangla = "তাদের পথ যাদেরকে তুমি নেয়ামত দান করেছ, তাদের পথ নয় যারা ক্রোধাক্রান্ত ও পথভ্রষ্ট।",
                    tafsirShort = "The path of Prophets, truthful ones, martyrs and righteous servants of Allah.",
                    words = listOf(
                        WordDetail(1, "صِرَاطَ", "Sirata", "The path of", "পথ"),
                        WordDetail(2, "الَّذِينَ", "Alladhina", "those", "যাদের"),
                        WordDetail(3, "أَنْعَمْتَ", "An'amta", "You bestowed favor", "নেয়ামত দিয়েছ"),
                        WordDetail(4, "عَلَيْهِمْ", "'Alayhim", "upon them", "তাদের ওপর")
                    )
                )
            )
            112 -> listOf(
                Ayah(
                    id = 1,
                    surahNumber = 112,
                    ayahNumber = 1,
                    textArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ",
                    textEnglish = "Say, \"He is Allah, [who is] One,",
                    textBangla = "বলুন, তিনি আল্লাহ, এক-অদ্বিতীয়।",
                    tafsirShort = "Proclamation of Tawhid - Allah is absolute and unique without partners."
                ),
                Ayah(
                    id = 2,
                    surahNumber = 112,
                    ayahNumber = 2,
                    textArabic = "اللَّهُ الصَّمَدُ",
                    textEnglish = "Allah, the Eternal Refuge.",
                    textBangla = "আল্লাহ কারো মুখাপেক্ষী নন, সকলেই তাঁর মুখাপেক্ষী।",
                    tafsirShort = "As-Samad means the Self-Sufficient upon Whom all creation depends."
                ),
                Ayah(
                    id = 3,
                    surahNumber = 112,
                    ayahNumber = 3,
                    textArabic = "لَمْ يَلِدْ وَلَمْ يُولَدْ",
                    textEnglish = "He neither begets nor is born,",
                    textBangla = "তাঁর কোনো সন্তান নেই এবং তিনি কারো সন্তান নন।",
                    tafsirShort = "Negation of offspring or parentage for the Supreme Creator."
                ),
                Ayah(
                    id = 4,
                    surahNumber = 112,
                    ayahNumber = 4,
                    textArabic = "وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                    textEnglish = "Nor is there to Him any equivalent.\"",
                    textBangla = "এবং তাঁর সমতুল্য কেউই নেই।",
                    tafsirShort = "Nothing is comparable to Allah in His essence, attributes, and power."
                )
            )
            else -> listOf(
                Ayah(
                    id = 1,
                    surahNumber = surahNumber,
                    ayahNumber = 1,
                    textArabic = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                    textEnglish = "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                    textBangla = "পরম করুণাময় অসীম দয়ালু আল্লাহর নামে শুরু করছি।",
                    tafsirShort = "Starting in the name of Allah brings blessings and peace."
                ),
                Ayah(
                    id = 2,
                    surahNumber = surahNumber,
                    ayahNumber = 2,
                    textArabic = "تَنزِيلُ الْكِتَابِ لَا رَيْبَ فِيهِ مِن رَّبِّ الْعَالَمِينَ",
                    textEnglish = "[This is] the revelation of the Book about which there is no doubt from the Lord of the worlds.",
                    textBangla = "এই কিতাব অবতীর্ণ হয়েছে বিশ্বজগতের প্রতিপালকের নিকট থেকে, এতে কোনো সন্দেহ নেই।",
                    tafsirShort = "Divine truth from the Lord of all creation."
                )
            )
        }
    }

    fun getPrayerTimes(): List<PrayerTime> {
        return listOf(
            PrayerTime("Fajr", "ফজর", "04:22 AM", "ic_fajr"),
            PrayerTime("Sunrise", "সূর্যোদয়", "05:42 AM", "ic_sunrise"),
            PrayerTime("Dhuhr", "যোহর", "12:15 PM", "ic_dhuhr", isNext = true),
            PrayerTime("Asr", "আসর", "04:35 PM", "ic_asr"),
            PrayerTime("Maghrib", "মাগরিব", "06:48 PM", "ic_maghrib"),
            PrayerTime("Isha", "ইশা", "08:10 PM", "ic_isha")
        )
    }

    fun getAllahNames(): List<AllahName> {
        return listOf(
            AllahName(1, "الرَّحْمَنُ", "Ar-Rahman", "The Most Gracious", "পরম করুণাময়", "Increases memory and focus when recited daily."),
            AllahName(2, "الرَّحِيمُ", "Ar-Raheem", "The Most Merciful", "অসীম দয়ালু", "Brings peace and relief from fear."),
            AllahName(3, "الْمَلِكُ", "Al-Malik", "The King / Sovereign", "সার্বভৌম ক্ষমতার অধিকারী", "Provides financial security and respect."),
            AllahName(4, "الْقُدُّوسُ", "Al-Quddus", "The Most Holy", "নিষ্কলঙ্ক / পরম পবিত্র", "Cleanses heart from anxiety and doubts."),
            AllahName(5, "السَّلاَمُ", "As-Salam", "The Giver of Peace", "শান্তি দানকারী", "Grants physical and spiritual health."),
            AllahName(6, "الْمُؤْمِنُ", "Al-Mu'min", "The Giver of Faith", "নিরাপত্তা ও ঈমান দানকারী", "Protects against all evil forces."),
            AllahName(7, "الْمُهَيْمِنُ", "Al-Muhaymin", "The Guardian", "রক্ষক ও তত্ত্বাবধানকারী", "Purifies spirit and inner wisdom."),
            AllahName(8, "الْعَزِيزُ", "Al-Aziz", "The Almighty", "পরাক্রমশালী", "Grants dignity and independence."),
            AllahName(9, "الْجَبَّارُ", "Al-Jabbar", "The Compeller", "দুর্নিবার ক্ষমতাশালী", "Protects against oppression and injustice.")
        )
    }

    fun getDailyDuas(): List<Dua> {
        return listOf(
            Dua(
                1, "Morning", "সকালের দোআ", "Morning Dua",
                "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ",
                "Asbahna wa asbahal mulku lillah, walhamdu lillah",
                "আমরা সকালে উপনীত হয়েছি এবং সমগ্র রাজত্ব আল্লাহর জন্যই নির্ধারিত, সমস্ত প্রশংসা আল্লাহর।",
                "We have reached the morning and at this very time all sovereignty belongs to Allah, praise be to Allah.",
                "Muslim 4/2088"
            ),
            Dua(
                2, "Night", "ঘুমানোর দোআ", "Dua Before Sleeping",
                "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                "Bismika Allahumma amutu wa ahya",
                "হে আল্লাহ! তোমার নামে আমি মৃত্যুবরণ করি (ঘুমাই) এবং তোমার নামেই জীবিত হই (জেগে উঠি)।",
                "In Your name, O Allah, I die and I live.",
                "Bukhari 6312"
            ),
            Dua(
                3, "Food", "খাবার শুরুর দোআ", "Dua Before Eating",
                "بِسْمِ اللهِ وَعَلَى بَرَكَةِ اللهِ",
                "Bismillahi wa 'ala barakatillah",
                "আল্লাহর নামে এবং আল্লাহর বরকতের সাথে খাওয়া শুরু করছি।",
                "In the name of Allah and with the blessings of Allah.",
                "Al-Hakim 4/108"
            )
        )
    }

    fun getHadiths(): List<Hadith> {
        return listOf(
            Hadith(
                1, "Sahih Al-Bukhari", "Revelation",
                "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ",
                "সকল কাজের ফলাফল নিয়তের ওপর নির্ভরশীল।",
                "Actions are judged by intentions.",
                "Umar ibn Al-Khattab (RA)"
            ),
            Hadith(
                2, "Sahih Muslim", "Faith",
                "الطُّهُورُ شَطْرُ الإِيمَانِ",
                "পবিত্রতা ঈমানের অর্ধেক।",
                "Purity is half of faith.",
                "Abu Malik Al-Ash'ari (RA)"
            )
        )
    }

    fun getNooraniLessons(): List<NooraniLesson> {
        return listOf(
            NooraniLesson(
                1, 1, "Single Arabic Letters (হরফ পরিচয)",
                "Learn the 29 standalone letters of the Arabic alphabet with proper makhraj (pronunciation points).",
                listOf("أ", "ب", "ت", "ث", "ج", "ح", "خ", "د", "ذ", "ر", "ز", "س", "ش", "ص", "ض", "ط", "ظ", "ع", "غ", "ف", "ق", "ك", "ل", "م", "ن", "هـ", "و", "ي"),
                "Makhraj 1: Al-Jawf (Empty space in mouth and throat for elongated vowels)."
            ),
            NooraniLesson(
                2, 2, "Harakat (হরেকত - যের, জবর, পেশ)",
                "Understanding Short Vowels: Fatha (Zabor), Kasra (Zer), and Damma (Pesh).",
                listOf("أَ", "إِ", "أُ", "بَ", "بِ", "بُ"),
                "Harakat are pronounced quickly without stretching the sound."
            )
        )
    }

    fun getArticles(): List<Article> {
        return listOf(
            Article(
                1, "Virtues of Reciting Surah Al-Kahf on Friday", "Virtues", "Islamic Scholar",
                "Reciting Surah Al-Kahf on Fridays illuminates light between the reciter and the Kaaba.",
                "Surah Al-Kahf contains four pivotal Quranic stories dealing with trials of faith, wealth, knowledge, and power. The Prophet (PBUH) stated: 'Whoever reads Surah Al-Kahf on the day of Jumu'ah, will have a light that will shine for him from one Friday to the next.'",
                "2026-07-20", 4
            ),
            Article(
                2, "Understanding Tajweed Rules Made Simple", "Learning", "Tajweed Specialist",
                "Essential rules of Noon Sakinah, Tanween, and Meem Sakinah.",
                "Tajweed literally means beautifying or enhancing. Applying Tajweed rules prevents mispronunciation of divine words and ensures the recitation mimics the Prophet Muhammad (PBUH).",
                "2026-07-22", 6
            )
        )
    }
}
