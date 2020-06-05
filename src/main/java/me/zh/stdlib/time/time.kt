package me.zh.stdlib.time

//import org.graalvm.compiler.debug.TTY.printf
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun foo() {
    var instant = Instant.now();
    println(instant)
    println(Instant.ofEpochSecond(20));
    println(LocalDate.now())
    println(LocalDate.of(2017, 1, 1))
    val date = LocalDate.ofYearDay(2017, 32)
    println(date.isLeapYear)
    println(date.dayOfMonth);
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    println(LocalDateTime.now().format(formatter))
//    println(LocalDateTime.now().plus())
    println(LocalDateTime.now().withDayOfYear(10).toString() + "====")
    val timestamp = java.sql.Timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
    timestamp.toLocalDateTime().format(formatter)
    val valueOf: Timestamp = Timestamp.valueOf(LocalDateTime.parse("20200601153126", DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
    valueOf.toLocalDateTime();


    println(valueOf.toString())
}

fun main() {
    foo()
}