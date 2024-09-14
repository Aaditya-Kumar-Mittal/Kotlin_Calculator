package com.example.kotlin_calculator

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CalcScreen : AppCompatActivity() {

    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_calc_screen)

        val calculatorLayout = findViewById<LinearLayout>(R.id.main)

        // Cast its background to AnimationDrawable and start the animation
        val gradientDraw = calculatorLayout.background as AnimationDrawable
        gradientDraw.setEnterFadeDuration(1000)
        gradientDraw.setExitFadeDuration(5000)
        gradientDraw.start()

        //Binding File name will be ActivityCalcScreenBinding
        //val binding : ActivityCalcScreenBinding
    }

    fun allClearAction(view: View){

        var workingsTV = findViewById<TextView>(R.id.workingsTV)
        var resultsTV = findViewById<TextView>(R.id.resultsTV)
        workingsTV.text = ""
        resultsTV.text = ""

    }

    fun backSpaceAction(view: View){
        var backBtn = findViewById<Button>(R.id.backButton)
        var workings = findViewById<TextView>(R.id.workingsTV)
        val length = workings.length()

        if (length > 0){
            workings.text = workings.text.substring(0 , length-1)
        }
    }

    fun numberAction(view: View){

        if (view is Button){

            if(view.text == "."){

                if(canAddDecimal){
                    var workingsTV = findViewById<TextView>(R.id.workingsTV)
                    workingsTV.append(view.text)
                }
                canAddDecimal = false
            }
            else{
                var workingsTV = findViewById<TextView>(R.id.workingsTV)
                workingsTV.append(view.text)
            }
            canAddOperation = true
        }

    }

    fun operationAction(view: View){
        if (view is Button){
            var workingsTV = findViewById<TextView>(R.id.workingsTV)
            workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun equalsAction(view :View){

        var resultsTV = findViewById<TextView>(R.id.resultsTV)

        resultsTV.text = calculateResults()

    }

    private fun calculateResults() : String{

        val digitsOperators = digitsOperators()

        if (digitsOperators.isEmpty()) return ""

        val multiplyDivision = multiplyDivisionCalculate(digitsOperators)

        if (multiplyDivision.isEmpty()) return ""

        val result = addSubtractCalculate(multiplyDivision)

        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>) : Float{

        var result = passedList[0] as Float

        for( i in passedList.indices)
        {

            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]

                val nextDigit =  passedList[i+1] as Float

                if(operator == '+'){

                    result += nextDigit
                }

                if(operator == '-'){

                    result -= nextDigit
                }
            }

        }

        return result
    }

    private fun multiplyDivisionCalculate(passedList : MutableList<Any>) : MutableList<Any>{

        var list = passedList

        while (list.contains('X') || list.contains('/')){

            list = calcTimesDiv(list)
        }

        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator)
                {
                    'X' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }



    private fun digitsOperators() : MutableList<Any>{

        var mutableList = mutableListOf<Any>()

        var workingsTV = findViewById<TextView>(R.id.workingsTV)

        var currentDigit = ""

        for (character in workingsTV.text){

            //If the character is a digit or a decimal point
            if(character.isDigit() || character == '.'){
                currentDigit += character
            }
            else{
                //Else the character will be an operator
                mutableList.add(currentDigit.toFloat())
                currentDigit = ""
                mutableList.add(character)
            }
        }

        if(currentDigit != ""){
            mutableList.add(currentDigit.toFloat())
        }

        return mutableList
    }
}