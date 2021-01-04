package io.devopsbox.infrastructure.test.common.cdk

class PropsFileSerializer {
    def serialize(File file, Serializable obj) {
        file.newObjectOutputStream().withCloseable { objOutputStream ->
            objOutputStream << obj
        }
    }

    Object deserialize(String path) {
        new File(path).newObjectInputStream().withCloseable { objInputStream ->
            return objInputStream.readObject()
        }
    }
}
