package com.lamaq.tasbeeh.util

val engToArabic = mapOf(
    1 to "١",
    2 to "٢",
    3 to "٣",
    4 to "٤",
    5 to "٥",
    6 to "٦",
    7 to "٧",
    8 to "٨",
    9 to "٩",
    0 to "٠"
)

fun convertToArabicDigits(input: String): String {
    val result = StringBuilder()
    var ignoreLeadingZero = true

    for (char in input) {
        if (char.isDigit()) {
            if (char != '0') {
                ignoreLeadingZero = false
                val arabicDigit = engToArabic[char.toString().toInt()]
                result.append(arabicDigit)
            } else if (!ignoreLeadingZero) {
                result.append(engToArabic[char.toString().toInt()])
            }
        } else {
            result.append(char)
            ignoreLeadingZero = false
        }
    }

    return result.toString()
}

val fullMonthName = mapOf(
    "Muharram" to "محرم الحرام",
    "Safar" to "صفر المظفر",
    "Rabiʻ I" to "ربيع الأول",
    "Rabiʻ II" to "ربيع الآخر",
    "Jumada I" to "جمادى الأولى",
    "Jumada II" to "جمادى الآخرة",
    "Rajab" to "رجب الأصب",
    "Shaʻban" to "شعبان الكريم",
    "Ramadan" to "رمضان المعظم",
    "Shawwal" to "شوال المكرم",
    "Dhuʻl-Qiʻdah" to "ذو القعدة الحرام",
    "Dhuʻl-Hijjah" to "ذو الحجة الحرام"
)

