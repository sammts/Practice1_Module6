package com.amaurypm.videogamesdb

/**
 * Creado por Amaury Perea Matsumura el 01/09/23
 */
fun main() {
    operaNumeros(52,26)
    operaNumeros(30,18)

    val miLambdaSuma: (Int, Int) -> Unit = { a, b -> println("La suma de $a + $b es: ${a+b}") }

    operaNumeros(20,10, miLambdaSuma, {})

    operaNumeros(20,10, { a, b ->
        println("La suma de $a + $b es: ${a + b}")
    }, { name ->
        println(name)
    })

    operaNumeros(20, 10, { a,b ->
        println("La multiplicación de $a * $b es: ${a*b}")
    }, { name ->
        println("Hola $name")
    })

}

fun operaNumeros(num1: Int, num2: Int){
    println("La suma de $num1 + $num2 es ${num1+num2}")
}

fun operaNumeros(num1: Int, num2: Int, operacion: (Int, Int) -> Unit, lambda2: (String) -> Unit){
    operacion(num1, num2)
    lambda2("Amaury")
}