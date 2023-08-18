package com.lamaq.tasbeeh.components

//val homeTasbeeh = listOf(
//    "تسبیح",
//)
//
//val ahlebait = listOf(
//    "يا محمد",
//    "يا علي",
//    "يا فاطمة",
//    "يا حسن",
//    "يا حسين",
//    "يا زينب",
//    "يا عباس",
//    "يا طيب",
//)
//
//val impNames = listOf(
//    "يا علي",
//    "يا فاطمة",
//    "يا حسين",
//)
//
//val shortNames = listOf(
//    "تسبیح",
//    "يا محمد",
//    "يا علي",
//    "يا فاطمة",
//    "يا حسن",
//    "يا حسين",
//    "يا زينب",
//    "يا عباس",
//    "يا طيب",
//)
//
//val longTasbeehs = mapOf(
//    "صلوات" to "أللهم صل على محمد و على أل محمد وبارك وسلم",
//)
//
//val tasbeehTypes = listOf(
//    "تسبیح",
//    "صلوات",
//    "أهل البيت",
//)
//
//val singleTasbeeh = listOf(
//    "تسبیح",
//    "صلوات",
//)
//
//val hasSub = mapOf(
//    "أهل البيت" to ("ahlebait") as DocumentReference,
//)

data class TasbeehData(
    val longTasbeehs: Map<*, *>,
    val shortNames: List<*>,
    val hasSub: Map<*, *>,
    val homeTasbeeh: List<*>,
    val impNames: List<*>,
    val singleTasbeeh: List<*>,
    val tasbeehTypes: List<*>
)
