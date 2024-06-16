package com.example.geoquiz

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geoquiz.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val questionBank = listOf(
        Questions(R.string.questionOne, true),
        Questions(R.string.questionTwo,false),
        Questions(R.string.questionThree, true)
    )
    private var currentIndex = 0
    private val totalScore = questionBank.size
    private var userScore = 0
    private var hasAnswered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.questionText.setText(questionBank[currentIndex].question)

        binding.questionText.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            showQuestion()
        }

        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            showQuestion()
            hasAnswered = false
        }
        binding.previousButton.setOnClickListener {
            if (currentIndex == 0) {
                Toast.makeText(this, "no previous question", Toast.LENGTH_SHORT).show()
            } else {
                currentIndex = (currentIndex -1) % questionBank.size
                showQuestion()
            }
        }

        binding.trueButton.setOnClickListener { view ->
            checkAnswer(view, true)
        }
        binding.falseButton.setOnClickListener { view ->
            checkAnswer(view, false)
        }
        val apiChar = "API Level is ${Build.VERSION.SDK_INT}"
        binding.apiTitle.setText(apiChar)
    }

    private fun showQuestion() {
        val nextQuestion = questionBank[currentIndex].question
        binding.questionText.setText(nextQuestion)
    }

    private fun checkAnswer(view: View, userInput:Boolean){
        if (hasAnswered) {
            Toast.makeText(this, R.string.user_answered_already, Toast.LENGTH_SHORT).show()
        } else {
            val correctAnswer = questionBank[currentIndex].answer
            val messageResId = if (userInput == correctAnswer) {
                R.string.correct_toast
            } else {
                R.string.incorrect_toast
            }
            if (userInput == correctAnswer) userScore += 1
            Snackbar.make(view, messageResId, Snackbar.LENGTH_SHORT).show()
            hasAnswered = true
        }
        //reached last question
        if (currentIndex == (questionBank.size - 1)) {
            val scores = userScore/totalScore * 100
            Toast.makeText(this, "$scores", Toast.LENGTH_LONG).show()
            userScore = 0
        }
    }
}