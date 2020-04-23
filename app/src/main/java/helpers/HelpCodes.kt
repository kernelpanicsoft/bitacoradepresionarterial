package helpers

object Genero{
    const val MASCULINO = 0
    const val FEMENINO = 1
}

object TipoRecordatorio{
    const val TOMA_PRESION = 0
    const val TOMA_MEDICAMENTO = 1
}

object Valoracion{
    const val HIPOTENSION = 0
    const val NORMAL = 1
    const val PREHIPERTENSION = 2
    const val HIPERTENSION_1 = 3
    const val HIPERTENSION_2 = 4
    const val CRISIS = 5

}

const val REGISTRAR_USUARIO = 1000
const val VISUALIZAR_USUARIO = 1001
const val ELIMINAR_USUARIO = 1002
const val SIN_USUARIO_ACTIVO = -1

const val DEFAULT_NOTIFICATION_CHANEL_ID = "primary_notification_channel"

object Ordenes{
    const val PREDETERMINADO = 0
    const val ANTIGUEDAD_ASC = 1
    const val ANTIGUEDAD_DESC = 2
}