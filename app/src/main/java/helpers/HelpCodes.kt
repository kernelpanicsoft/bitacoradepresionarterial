package helpers

object Genero{
    const val MASCULINO = 0
    const val FEMENINO = 1
}

object TipoRecordatorio{
    const val TOMA_PRESION = 0
    const val TOMA_MEDICAMENTO = 1
}



const val REGISTRAR_USUARIO = 1000
const val VISUALIZAR_USUARIO = 1001
const val ELIMINAR_USUARIO = 1002
const val SIN_USUARIO_ACTIVO = -1

const val DEFAULT_NOTIFICATION_CHANEL_ID = "primary_notification_channel"

object Ordenes{
    const val PREDETERMINADO = 99
    const val ANTIGUEDAD_ASC = 98
    const val ANTIGUEDAD_DESC = 97
}

object Filtros{
    const val PREDETERMINADO = 0
    const val VALORACION = 1
    const val MOMENTO = 2
    const val EXTREMIDAD = 3
    const val POSICION = 4
}

object Valoracion{
    const val HIPOTENSION = 1000
    const val NORMAL = 1001
    const val PREHIPERTENSION = 1002
    const val HIPERTENSION_1 = 1003
    const val HIPERTENSION_2 = 1004
    const val CRISIS = 1005
}

object Momento{
    const val ALEATORIO = 2000
    const val DESPUES_DESPERTAR = 2001
    const val ANTES_DORMIR = 2002
    const val ANTES_MEDICAMENTO = 2003
    const val DESPUES_MEDICAMENTO = 2004
    const val ANTES_COMER = 2005
    const val DESPUES_COMER = 2006

}

object Extremidad{
    const val BRAZO_IZQUIERDO = 3000
    const val BRAZO_DERECHO = 3001
    const val MUNECA_IZQUIERDA = 3002
    const val MUNECA_DERECHA = 3003
    const val TOBILLO_IZQUIERDO = 3004
    const val TOBILLO_DERECHO = 3005
    const val MUSLO_DERECHO = 3006
    const val MUSLO_IZQUIERDO = 3007
}

object Posicion{
    const val SENTADO = 4000
    const val ACOSTADO = 4001
    const val RECOSTADO = 4002
    const val DE_PIE = 4003
}

