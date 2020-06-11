package com.example.numberbaseballgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : BaseActivity() {

    //컴퓨터가 낸 문제 숫자 세 개를 저장할 ArrayList
    val computerNumbers = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setValues()
        setupEvents()
    }
    override fun setValues() {
//문제를 내라고 컴퓨터에게 시킴 =>문제:3자리 숫자 배열
        makeComputerNumber()
    }

    override fun setupEvents() {

    }
    
    fun makeComputerNumber()
    {
        //숫자 세 개를 랜덤 생성=>3번 반복
        for(i in 0..2)
        {

            //규칙에 맞는 숫자를 뽑을때 까지 무한 반복
            while(true) {

                // TODO : 1~9 숫자 랜덤으로 뽑기
                val randomNum = (Math.random() * 9 +1).toInt() // 0<= Math.random() <9 => Math.random()*9+1, toInt():버림
                Log.d("랜덤값", randomNum.toString())
                
                //숫자가 유효한지 저장할 변수
                var isNumberOK = true


                // TODO : 숫자를 써도 되는지 검사
                //중복체크 => 중복된 숫자는 무효
                for(tmp in computerNumbers)
                {
                    if(randomNum==tmp){
                        isNumberOK = false
                        break
                    }
                }

                
                //조건에 맞는 숫자가 찾아지면 무한반복 맘춤
                if(isNumberOK)
                {
                    computerNumbers.add(randomNum)
                    break
                }

            }

        }
    }
}
