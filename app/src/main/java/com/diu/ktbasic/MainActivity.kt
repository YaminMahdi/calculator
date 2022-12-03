package com.diu.ktbasic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    lateinit var num1: EditText
    lateinit var num2: EditText
    lateinit var sum: Button
    lateinit var sub: Button
    lateinit var mul: Button
    lateinit var div: Button
    lateinit var per: Button

    lateinit var ans: TextView
    var n1: Int? = null
    var n2: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        num1 = findViewById(R.id.in1)
        num2 = findViewById(R.id.in2)
        sum = findViewById(R.id.sum)
        sub = findViewById(R.id.sub)
        mul = findViewById(R.id.mul)
        div = findViewById(R.id.div)
        per = findViewById(R.id.per)

        ans = findViewById(R.id.output)

        sum.setOnClickListener { cal(it) }
        sub.setOnClickListener { cal(it) }
        mul.setOnClickListener { cal(it) }
        div.setOnClickListener { cal(it) }
        per.setOnClickListener { cal(it) }




    }

    private fun cal(it: View) {
        if (num1.text.isNullOrEmpty() || num2.text.isNullOrEmpty())
            Toast.makeText(this, "Please Input", Toast.LENGTH_SHORT).show()
        else {
            n1 = num1.text.toString().toInt()
            n2 = num2.text.toString().toInt()
            if (it.id == R.id.sum)
                ans.text = (n1!! + n2!!).toString()
            else if (it.id == R.id.sub)
                ans.text = (n1!! - n2!!).toString()
            else if (it.id == R.id.mul)
                ans.text = (n1!! * n2!!).toString()
            else if (it.id == R.id.div)
                ans.text = (n1!! / n2!!).toString()
            else
                ans.text = (n1!! * (n2!! / 100.0)).toString()
        }
    }
}