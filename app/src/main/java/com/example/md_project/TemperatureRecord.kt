package com.example.md_project

import android.content.Context
import com.google.gson.Gson
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class TemperatureRecord(val date: String, val temperature: Float)


fun saveTemperatureRecord(newRecord: TemperatureRecord, context: Context) {
    val records = loadTemperatureRecords(context).toMutableList()

    //Checks if a record with the same date already exists
    val existingRecordIndex = records.indexOfFirst {
        LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE) ==
                LocalDate.parse(newRecord.date, DateTimeFormatter.ISO_DATE)
    }

    if (existingRecordIndex != -1) {
        records[existingRecordIndex] = newRecord
    } else {
        records.add(newRecord)
    }

    //Ensured that only the latest 7 records are kept
    val sortedRecords = records
        .sortedBy { LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE) }
        .takeLast(7)

    //Serializes to JSON and saves it
    val jsonString = Gson().toJson(sortedRecords)
    context.openFileOutput("temperature_records.json", Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }
}


fun loadTemperatureRecords(context: Context): List<TemperatureRecord> {
    val fileName = "temperature_records.json"

    //Checks if the file exists
    val file = File(context.filesDir, "temperature_records.json")
    if (!file.exists()) {
        return emptyList()
    }

    val jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
    return Gson().fromJson(jsonString, Array<TemperatureRecord>::class.java).toList()
}

fun deleteTemperatureRecords(context: Context){ //Deletes all temperatureRecords (For testing purposes)
    val emptyRecordsList = emptyList<TemperatureRecord>()

    val jsonString = Gson().toJson(emptyRecordsList)

    //Writes the empty JSON to the file
    context.openFileOutput("temperature_records.json", Context.MODE_PRIVATE).use {
        it.write(jsonString.toByteArray())
    }
}