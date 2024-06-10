package com.example.apptranslate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.apptranslate.API.retrofitService
import com.example.apptranslate.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var alllanguages = emptyList<Language>() //lista que almacena los lenguages devueltos por la api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initListener()
        getlanguages()
    }

    private fun initListener() {
        binding.btnDetectLanguage.setOnClickListener{ //crea un listener en el boton para enviar la palabara a la API
            val text : String = binding.etDescription.text.toString() //variable que contiene la informacion escrita en el editable text del layout
            if (text.isNotEmpty()){ //si la informacion de la variable no esta vacia
                showLoading() //muestro el progressbar
                getTextLanguage(text)
            }
        }
    }

    private fun showLoading() { //funcion que muestra la carga del progressbar
        binding.ProgressBar.visibility = View.VISIBLE //indico que el progressbar sea visible
    }

    private fun getTextLanguage(text: String) {
        CoroutineScope(Dispatchers.IO).launch { //creo una coroutina que no estaen el hilo principal
            val result : retrofit2.Response<DetectionResponse> = retrofitService.getTextLanguage(text) //variable que contiene la respuesta del texto pasado por parametro
            if (result.isSuccessful){ //si salio todo correcto en la peticion a la api
                checkResult(result.body()) //funcion que contiene la respuesta definitiva de la palabra o frase enviada

            }else{ //si no salio bien la peticion
                showError() //funcion que muestra mensaje de error con un toast
            }
            cleanText()//funcion que elimina el contenido enviado a la api del editabletext del layout
            hideLoading() //funcion que esconde el progressbar
        }
    }

    private fun cleanText() { //funcion que elimina el contenido del editableText
        binding.etDescription.setText("")//asigno un campo vacio al contenido del texto
    }

    private fun hideLoading() { //funcion que esconde el progressbar
        runOnUiThread { //indico que lo que contiene se corra en el hilo principal del programa
            binding.ProgressBar.visibility = View.GONE //indico que el progressbar no sea visible
        }
    }

    private fun checkResult(detectionResponse: DetectionResponse?) {
        if(detectionResponse != null && !detectionResponse.data.detections.isNullOrEmpty()){ //verifico si el detectionresponse no esta vacia o es null
            val correctLanguages: List<Detections> = detectionResponse.data.detections.filter { it.isReliable } //variable que contiene los item filtrados que la api detecta que es mas posible que sea el idioma
            if (correctLanguages.isNotEmpty()){ //si la variable creada anteriormente no esta vacia

                val languageName : Language? = alllanguages.find { it.code == correctLanguages.first().language} //variable que busca en la lista de lenguages devuelta por la api aquel que tenga el mismo codigo
                if (languageName != null) {//si la variable que contiene el idioma no es igual a null
                    runOnUiThread{//indico que lo que contiene se corra en el hilo principal del programa
                        Toast.makeText(this,"EL IDIOMA ES: ${languageName.name}", Toast.LENGTH_LONG).show() //toast que muestra el resultado del idioma
                    }
                }
            }
        }
    }

    private fun getlanguages(){ //funcion que hace una peticion pata obtener el lenguage de la API

        CoroutineScope(Dispatchers.IO).launch { //creo una coroutina que no estaen el hilo principal
            val languages : retrofit2.Response<List<Language>> = retrofitService.getLanguages() //variable que obtiene todo los lenguages devueltos por la api

            if (languages.isSuccessful){ //si salio todo correcto
                alllanguages = languages.body() ?: emptyList() //si salio todo correcto almaceno la lista devuelta en la creada
                showSuccess()

            }else{ //si no salio bien la peticion
                showError() //funcion que muestra mensaje de error con un toast

            }
        }
    }

    private fun showSuccess() {
        runOnUiThread{ //indico que lo que contiene se corra en el hilo principal del programa
            Toast.makeText(this,"LISTO PARA PETICIONES", Toast.LENGTH_LONG).show()
        }
    }

    private fun showError() {
        runOnUiThread{ //indico que lo que contiene se corra en el hilo principal del programa
            Toast.makeText(this,"ERROR AL HACER LA LLAMADA", Toast.LENGTH_LONG).show()
        }
    }
}