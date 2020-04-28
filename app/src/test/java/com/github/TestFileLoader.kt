package com.github

import java.io.IOException

object TestFileLoader {

    /**
     * Load a String json file from the resources folder folder.
     *
     * @param fileName Name of the file without any path..
     * @return
     * @throws IOException
     */
    fun readJsonFileFromAssets(fileName: String): String {
        val inputStream = TestFileLoader::class.java.getResourceAsStream("/$fileName")
        val size = inputStream!!.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        return String(buffer)
    }
}