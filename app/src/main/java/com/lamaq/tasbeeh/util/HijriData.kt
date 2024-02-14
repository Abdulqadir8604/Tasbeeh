package com.lamaq.tasbeeh.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val Table1Stage1 = mapOf(
    0 to 8231, 30 to 18862, 60 to 29493, 90 to 40124, 120 to 50755, 150 to 61386, 180 to 72017, 210 to 82648,
    240 to 93279, 270 to 103910, 300 to 114541, 330 to 125172, 360 to 135803, 390 to 146434, 420 to 157065,
    450 to 167696, 480 to 178327, 510 to 188958, 540 to 199589, 570 to 210220, 600 to 220851, 630 to 231482,
    660 to 242113, 690 to 252744, 720 to 263375, 750 to 274006, 780 to 284637, 810 to 295268, 840 to 305899,
    870 to 316530, 900 to 327161, 930 to 337792, 960 to 348423, 990 to 359054, 1020 to 369685, 1050 to 380316,
    1080 to 390947, 1110 to 401578, 1140 to 412209, 1170 to 422840, 1200 to 433471, 1230 to 444102, 1260 to 454733,
    1290 to 465364, 1320 to 475995, 1350 to 486626, 1380 to 497257, 1410 to 507888, 1440 to 518519, 1470 to 529150,
    1500 to 539781
)

val Table1Stage2 = mapOf(
    1 to 0, 2 to 354, 3 to 709, 4 to 1063, 5 to 1417, 6 to 1772, 7 to 2126, 8 to 2480, 9 to 2835, 10 to 3189,
    11 to 3544, 12 to 3898, 13 to 4252, 14 to 4607, 15 to 4961, 16 to 5315, 17 to 5670, 18 to 6024, 19 to 6378,
    20 to 6733, 21 to 7087, 22 to 7442, 23 to 7796, 24 to 8150, 25 to 8505, 26 to 8859, 27 to 9213, 28 to 9568,
    29 to 9922, 30 to 10277
)

val Table1Stage3v1 = mapOf(
    "محرم الحرام" to 0, "صفر المظفر" to 30, "ربيع الأول" to 59, "ربيع الآخر" to 89,
    "جمادى الأولى" to 118, "جمادى الآخرة" to 148, "رجب الأصب" to 177, "شعبان الكريم" to 207,
    "رمضان المعظم" to 236, "شوال المكرم" to 266, "ذو القعدة الحرام" to 295, "ذو الحجة الحرام" to 325
)

val Table2Stage1 = mapOf(
    600 to 0, 700 to 36525, 800 to 73050, 900 to 109575, 1000 to 146100, 1100 to 182625,
    1200 to 219150, 1300 to 255675, 1400 to 292200, 1500 to 328725, 1582 to 358665,
    1600 to 365240, 1700 to 401764, 1800 to 438288, 1900 to 474812, 2000 to 511337
)

val Table2Stage2 = mapOf(
    4 to 1461, 8 to 2922, 12 to 4383, 16 to 5844, 20 to 7305, 24 to 8766, 28 to 10227, 32 to 11688,
    36 to 13149, 40 to 14610, 44 to 16071, 48 to 17532, 52 to 18993, 56 to 20454, 60 to 21915,
    64 to 23376, 68 to 24837, 72 to 26298, 76 to 27759, 80 to 29220, 84 to 30681, 88 to 32142,
    92 to 33603, 96 to 35064
)

val Table2Stage3v1 = mapOf(
    Pair(1, 0) to 0, Pair(1, 1) to 366, Pair(1, 2) to 731, Pair(1, 3) to 1096,
    Pair(2, 0) to 31, Pair(2, 1) to 397, Pair(2, 2) to 762, Pair(2, 3) to 1127,
    Pair(3, 0) to 60, Pair(3, 1) to 425, Pair(3, 2) to 790, Pair(3, 3) to 1155,
    Pair(4, 0) to 91, Pair(4, 1) to 456, Pair(4, 2) to 821, Pair(4, 3) to 1186,
    Pair(5, 0) to 121, Pair(5, 1) to 486, Pair(5, 2) to 851, Pair(5, 3) to 1216,
    Pair(6, 0) to 152, Pair(6, 1) to 517, Pair(6, 2) to 882, Pair(6, 3) to 1247,
    Pair(7, 0) to 182, Pair(7, 1) to 547, Pair(7, 2) to 912, Pair(7, 3) to 1277,
    Pair(8, 0) to 213, Pair(8, 1) to 578, Pair(8, 2) to 943, Pair(8, 3) to 1308,
    Pair(9, 0) to 244, Pair(9, 1) to 609, Pair(9, 2) to 974, Pair(9, 3) to 1339,
    Pair(10, 0) to 274, Pair(10, 1) to 639, Pair(10, 2) to 1004, Pair(10, 3) to 1369,
    Pair(11, 0) to 305, Pair(11, 1) to 670, Pair(11, 2) to 1035, Pair(11, 3) to 1400,
    Pair(12, 0) to 335, Pair(12, 1) to 700, Pair(12, 2) to 1065, Pair(12, 3) to 1430
)

fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return currentDate.format(formatter)
}

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

//fun getPrayerTimes() {
//
//}

fun getMisriDate(): String {
    try {
        // gregorian date
        val gregDate = getCurrentDate()

        val gCentury = gregDate.substring(6, 8).toInt() * 100
        val ordinalCorrespondingCentury = Table2Stage1[gCentury]!!

        val gCenturyYear = gregDate.substring(8, 10).toInt()
        val nearestLowerKeyForGCentury = Table2Stage2.keys.filter { it <= gCenturyYear }.max()
        val ordinalCorrespondingGCenturyYear = Table2Stage2[nearestLowerKeyForGCentury]!!

        val gMonth = gregDate.substring(3, 5)
        val yearDifference = gCenturyYear - nearestLowerKeyForGCentury
        val ordinalCorrespondingDifferenceInYear =
            Table2Stage3v1[Pair(gMonth.toInt(), yearDifference)]!!

        val gDay = gregDate.substring(0, 2).toInt()

        val ordinalDate =
            ordinalCorrespondingCentury + ordinalCorrespondingGCenturyYear + ordinalCorrespondingDifferenceInYear + gDay

        // misri date
        val nearestLowerKeyForOrdinalDate = Table1Stage1.values.filter { it <= ordinalDate }.max()
        val mCentury = Table1Stage1.entries.first { it.value == nearestLowerKeyForOrdinalDate }.key

        val mYearOrdinalDifference = ordinalDate - nearestLowerKeyForOrdinalDate
        val nearestLowerKeyForMYear =
            Table1Stage2.values.filter { it <= mYearOrdinalDifference }.max()
        val mCenturyYear = Table1Stage2.entries.first { it.value == nearestLowerKeyForMYear }.key
        val mYear = mCenturyYear + mCentury

        val mMonthOrdinalDifference = mYearOrdinalDifference - nearestLowerKeyForMYear
        val nearestLowerKeyForMMonth =
            Table1Stage3v1.values.filter { it <= mMonthOrdinalDifference }.max()
        val mMonth = Table1Stage3v1.entries.first { it.value == nearestLowerKeyForMMonth }.key

        val mDay = mMonthOrdinalDifference - nearestLowerKeyForMMonth

        println("Gregorian Date: $gregDate")

        val hour = LocalTime.now().hour
        return if (hour in 19..23 || hour in 0..6) {
            convertToArabicDigits("${mDay + 1} رات $mMonth $mYear")
        } else {
            convertToArabicDigits("$mDay $mMonth $mYear")
        }
    } catch (e: Exception) {
        return "Date: Error"
    }
}
