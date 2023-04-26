package search

import java.io.File
enum class Type(val value: String){
    ANY("ANY"),
    NONE("NONE"),
    ALL("ALL"),
    NULL("")
}
fun main(args: Array<String>) {
    val listAny = mutableListOf<String>()
    val searchIndex = mutableMapOf<String, MutableSet<Int>>()
    if(args.size != 0) {
        if(args[0] == "--data") {
            val file = File(args[1])
            if (file.exists()) { // checks if file exists
                val lines = file.readLines()
                for (line in lines) listAny.add(line)
            }
        }

    }else {
        val a = mutableSetOf(1)
        a.add(1)
        println(a)
        println("Enter the number of people:")
        val num = readln().toInt()
        println("Enter all people:")
        for (i in 0 until num) listAny.add(readln())
        println()
    }
    fun buildSearchIndex() {
        for (i in listAny.indices) {
            val words = listAny[i].split(" ")
            for (w in words) {
                if (searchIndex.contains(w)) {
                    searchIndex[w]?.add(i)
                } else searchIndex[w] = mutableSetOf(i)
            }
        }
    }
    fun search() {

        var found = mutableSetOf<String>()
        println("Select a matching strategy: ALL, ANY, NONE")
        val a = Type.valueOf(readln().uppercase())
        //println(a)
        println("Enter a name or email to search all suitable people.")
        val input = readln().lowercase().split(" ")
        fun any(): MutableSet<String> {
            val find = mutableSetOf<Int>()
            for (word in input) {
                val search = word.toRegex()
                for ((k, v) in searchIndex) {
                    if (search.matches(k.lowercase())) {
                        for (n in v) find.add(n)
                    }
                }
            }
            val temp = mutableSetOf<String>()
            for (i in find) temp.add(listAny[i])
            return temp
        }
        fun all(): MutableSet<String> {
            val temp = any()
            val temp2 = mutableListOf<MutableSet<String>>()
            for (s in temp) temp2.add(s.split(" ").toMutableSet())
            val results = mutableSetOf<MutableSet<String>>()
            list@for (list in temp2) {
                var find = true
                for (s in list) {
                    word@ for (word in input) {
                        if (s.lowercase() == word) {
                            find = true
                            break
                        }
                        find = false
                    }
                    if (!find) {
                        break
                    }
                }
                if (find) {
                    results.add(list)
                }
            }
            val ret = mutableSetOf<String>()
            for (list in results) {
                var sum = ""
                for (j in list) sum += "$j "
                ret.add(sum)
                sum = ""
            }
            return ret
        }
        fun none(): MutableSet<String> {
            val temp = any()
            val none = listAny
            for (i in temp) none.remove(i)
            return none.toMutableSet()
        }

        if (a == Type.ANY) found = any()
        else if (a == Type.ALL) found = all()
        else if (a == Type.NONE) found = none()

        if (found.size != 0) {
            println("\n${found.size} persons found:")
            for (i in found) println(i)
        } else println("No matching people found.")
        println()
    }
    fun listAll() {
        println("=== List of people ===")
        for (p in listAny) {
            println(p)
        }
    }
    fun menu() {
        println("""1. Find a person
2. Print all people
0. Exit""")
    }
    //listAny()
    buildSearchIndex()
    while (true) {

        menu()
        when (readln()) {
            "1" -> search()
            "2" -> listAll()
            "0" -> {
                println("Bye!")
                return
            }
            else -> println("Incorrect option! Try again.")
        }
        println()
    }
    }

