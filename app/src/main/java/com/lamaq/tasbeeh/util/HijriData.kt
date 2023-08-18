package com.lamaq.tasbeeh.util
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Welcome (
    val code: Long,
    val status: String,
    val data: Data
)

data class Data (
    val hijri: Hijri,
    val gregorian: Gregorian
)

data class Gregorian (
    val date: String,
    val format: String,
    val day: String,
    val weekday: GregorianWeekday,
    val month: GregorianMonth,
    val year: String,
    val designation: Designation
)

data class Designation (
    val abbreviated: String,
    val expanded: String
)

data class GregorianMonth (
    val number: Long,
    val en: String
)

data class GregorianWeekday (
    val en: String
)

data class Hijri (
    val date: String,
    val format: String,
    val day: String,
    val weekday: HijriWeekday,
    val month: HijriMonth,
    val year: String,
    val designation: Designation,
    val holidays: List<Any?>
)

data class HijriMonth (
    val number: Long,
    val en: String,
    val ar: String
)

data class HijriWeekday (
    val en: String,
    val ar: String
)

interface ApiService {
    @GET("v1/gToH/{date}")
    fun convertToHijri(@Path("date") date: String): Call<Welcome>
}

fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return currentDate.format(dateFormatter)
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

val fullMonthName = mapOf(
    "1" to "محرم الحرام",
    "2" to "صفر المظفر",
    "3" to "ربيع الأول",
    "4" to "ربيع الآخر",
    "5" to "جمادى الأولى",
    "6" to "جمادى الآخرة",
    "7" to "رجب الأصب",
    "8" to "شعبان الكريم",
    "9" to "رمضان المعظم",
    "10" to "شوال المكرم",
    "11" to "ذو القعدة الحرام",
    "12" to "ذو الحجة الحرام"
)

