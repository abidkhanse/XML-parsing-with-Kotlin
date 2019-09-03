package com.xmlparser.main

import Student
import StudentXmlParser


fun addStudents(studentXmlParser: StudentXmlParser){

    val abid = Student("cs-1", "Abid", "00460987654", "CS")
    var success = studentXmlParser.insertNode(abid)

    println("Record inserted $success")

    val khan = Student("cs-2", "Khan", "00461112222333", "CS")
    success = studentXmlParser.insertNode(khan)

    println("Record inserted $success")

    val peter = Student("cs-3", "Peter", "004667548543", "CS")
    success = studentXmlParser.insertNode(peter)

    println("Record inserted $success")

    if(studentXmlParser.saveXMLFile()){
        println("File Saved")
    }
}

fun updateStudent(studentXmlParser: StudentXmlParser ){

    val peter = Student("se-3", "Peter", "004667548543", "SE")

    val success  = studentXmlParser.updateNode("cs-3", peter)

    if(success){
        studentXmlParser.saveXMLFile()
    }

    println("Record updated $success")
}

fun removeStudent(studentXmlParser: StudentXmlParser ){

    val success  = studentXmlParser.removeNode("se-3")

    if(success){
        studentXmlParser.saveXMLFile()
    }

    println("Record removed $success")
}


fun main() {

    var filename = "Source/students.xml"

    val studentXmlParser = StudentXmlParser(filename)

    addStudents(studentXmlParser)

    //updateStudent(studentXmlParser)

    //removeStudent(studentXmlParser)

    //studentXmlParser.printXMLFile()

}