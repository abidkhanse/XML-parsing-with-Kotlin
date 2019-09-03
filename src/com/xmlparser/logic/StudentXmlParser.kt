import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class StudentXmlParser{

    companion object {

        var ROOT = "students"
        var NODE = "student"

        var ID = "id"
        var NAME = "name"
        var PHONE = "phone"
        var DEPARTMENT = "department"
    }

    var fileName : String
    var xmlDocument : Document

    constructor(filename : String){

        this.fileName = filename

        val documentBuilderFactory = DocumentBuilderFactory.newInstance()

        val file = File(filename)

        val documentBuilder = documentBuilderFactory.newDocumentBuilder()

        xmlDocument = if (!file.exists()) {

            documentBuilder.newDocument()

        } else {

            documentBuilder.parse(file)
        }
    }

    fun insertNode(student : Student) : Boolean {

        var inserted = false

        var root = xmlDocument.documentElement

        if(root == null){

            root = xmlDocument.createElement(ROOT)

            xmlDocument.appendChild(root)

        }

        val newNode = createNode(student)

        if(root.appendChild(newNode) != null){

            inserted = true
            xmlDocument.replaceChild(root, root)

        }

        return inserted
    }


    fun createNode(student : Student) : Element {

        val node = xmlDocument.createElement(NODE)

        node.setAttribute(ID, student.id)

        node.appendChild(createChildElement(NAME, student.name))

        node.appendChild(createChildElement(PHONE, student.phone))

        node.appendChild(createChildElement(DEPARTMENT, student.department))

        return node

    }

    private fun createChildElement(tag: String, value: String): Element {

        val element = xmlDocument.createElement(tag)

        element.appendChild(xmlDocument.createTextNode(value))

        return element

    }

    fun saveXMLFile() : Boolean {

        var success = false

        if ( xmlDocument.hasChildNodes()) {

            val transformerFactory = TransformerFactory.newInstance()

            try {

                val transformer = transformerFactory.newTransformer()

                transformer.setOutputProperty(OutputKeys.INDENT, "yes")
                transformer.setOutputProperty(OutputKeys.VERSION, "1.0")
                transformer.setOutputProperty(OutputKeys.STANDALONE, "no")

                val domSource = DOMSource(xmlDocument)

                val streamResult = StreamResult(File(fileName))

                transformer.transform(domSource, streamResult)

                success = true

            } catch (e: Exception){
                e.printStackTrace()
            }
        }

        return success
    }

    fun printXMLFile(){

        val nodeList = xmlDocument.documentElement.childNodes

        for (i in 0 until nodeList.length){

            val node = nodeList.item(i)

            if(node.nodeType == Node.ELEMENT_NODE){

                val element = node as Element
                printElement (element)
            }
        }
    }

    private fun printElement(element: Element){

        val id = element.getAttribute(ID);
        val name = getElementsByTagName(element, NAME)
        val phone = getElementsByTagName(element, PHONE)
        val department = getElementsByTagName(element, DEPARTMENT)

        println("Student ID = $id Name = $name Phone = $phone department = $department" )

    }


    private fun getElementsByTagName(element: Element, tag: String) : String{

        val element = element.getElementsByTagName(tag).item(0).textContent
        return element
    }

    fun updateNode(id : String, newObj : Student) : Boolean{

        var updated = false

        var root = xmlDocument.documentElement

        val nodeList = root.childNodes

        for (i in 0 until nodeList.length){

            val node = nodeList.item(i)

            if(node.nodeType == Element.ELEMENT_NODE){

                val element = node as Element

                if(element.getAttribute(ID) == id){

                    val newElement = createNode(newObj)

                    if(root.replaceChild(newElement, element) != null){

                        xmlDocument.replaceChild(root, root)

                        updated = true
                    }
                    break
                }
            }
        }

        return updated
    }

    fun removeNode(id : String) : Boolean {

        var removed = false

        val root = xmlDocument.documentElement

        val nodeList = root.childNodes

        for (i in 0 until nodeList.length){

            val node = nodeList.item(i)

            if(node.nodeType == Element.ELEMENT_NODE){

                val element = node as Element

                if(element.getAttribute(ID) == id){

                    if(root.removeChild(element) != null){

                        xmlDocument.replaceChild(root, root)
                        removed = true

                    }

                    break
                }

            }

        }

        return removed
    }


}