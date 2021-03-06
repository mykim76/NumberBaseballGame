package com.example.numberbaseballgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.numberbaseballgame.adapters.ChatAdapter
import com.example.numberbaseballgame.datas.Chat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    //시도한 숫자를 저장하는 배열 => 중복 입력 체크
    val stryNumberStrArr = ArrayList<String>()
    // how many times?
    var inputCount = 0
    //컴퓨터가 낸 문제 숫자 세 개를 저장할 ArrayList
    val computerNumbers = ArrayList<Int>()

    val chatMessageList = ArrayList<Chat>()
    lateinit var mChatAdapter:ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setValues()
        setupEvents()
    }
    override fun setValues() {

        mChatAdapter = ChatAdapter(mContext,R.layout.chat_list_item, chatMessageList)
        chatListView.adapter = mChatAdapter


        //문제를 내라고 컴퓨터에게 시킴 =>문제:3자리 숫자 배열
        makeComputerNumber()
    }

    override fun setupEvents() {

        btnOK.setOnClickListener { 
            //사용자가 입력한 값을 String으로 저장
            val inputNumStr = inputNumberEdt.text.toString()

            //입력값의 타당성 체크
            if(inputNumStr.length != 3)
            {
                Toast.makeText(mContext,"숫자는 세 자리여야 합니다", Toast.LENGTH_SHORT).show()
                inputNumberEdt.setText("")
                return@setOnClickListener //리턴타입이 없는 함수에서는 함수를 강제 종료시키는 키워드를 사용
            }

            //0이 포함되어 있는지 체크
            if(inputNumStr.contains("0"))
            {
                Toast.makeText(mContext,"0은 문제에 포함되지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //이미 시도한 숫자 체크
            for(triedStr in stryNumberStrArr)
            {
                if(triedStr==inputNumStr)
                {
                    Toast.makeText(mContext,"${inputNumStr} : 이미 시도한 수입니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                }
            }

            stryNumberStrArr.add(inputNumStr)

            //사용자 입력한 숫자를 채팅 메세지로 변환
            val userChag = Chat("USER", inputNumStr)



            Handler().postDelayed({
                //채팅 메세지를 채팅 내역 배열에 추가
                chatMessageList.add(userChag)

                //ListView 내용이 바뀌면 새로고침
                mChatAdapter.notifyDataSetChanged()
            }, 300)


            //답장 후 바닥(리스트 목록 가장 마지막)으로 리스트를 끌어내리자
            chatListView.smoothScrollToPosition(chatMessageList.size-1)



            //입력하고 나면 editText 내용을 빈간
            inputNumberEdt.setText("")//EditText 는 text=String이 잘 먹지 않음

            //입력값 판단할 함수 호출
            checkStrikeAndBall(inputNumStr.toInt())
            
        }
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
                //Log.d("랜덤값", randomNum.toString())
                
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

        //문제가 뭔지 확인하기
        for(num in computerNumbers)
        {
            Log.d("최종번호", num.toString())
        }
        
        //문제를 다 내고, 안내 메세지를 채팅으로 출력
        chatMessageList.add(Chat("CPU","숫자 야구 게임에 오신 것을 환영합니다."))
        mChatAdapter.notifyDataSetChanged()
        Handler().postDelayed({
            chatMessageList.add(Chat("CPU","제가 생각하는 세자리 숫자를 맞춰주세요"))
            mChatAdapter.notifyDataSetChanged()
        }, 1000)

        Handler().postDelayed({
            chatMessageList.add(Chat("CPU","1~9의 숫자로만 구성되고, 중복된 숫자는 없습니다."))
            mChatAdapter.notifyDataSetChanged()
        }, 2000)

    }

    //입력 값을 계산해서 리스트뷰에 답장 띄우기
    fun checkStrikeAndBall(inputNum:Int){

        inputCount ++
        //inputNum에는 세 자리 숫자가 들어온다고 전제
        //3자리 숫자를 3칸의 배열로 분리
        val inputNumArry = ArrayList<Int>()
        //100,10,1 자리
        inputNumArry.add(inputNum/100)
        inputNumArry.add((inputNum/10)%10)
        inputNumArry.add(inputNum%10)

        var ballCount = 0
        var strikeCount = 0
        for(i in inputNumArry.indices)
        {
            for(j in computerNumbers.indices){
                if(inputNumArry[i]==computerNumbers[j])
                {
                    if(i==j)
                    {
                        strikeCount++
                    }
                    else{
                        ballCount++
                    }
                }
            }
        }

        //결과를 채팅메세지로 가공해서 컴퓨터가 답장한 내용으로 추가
        val answer = Chat("CPU","${strikeCount}S ${ballCount}B")
        //답장하기 전 시간적 여백 추가
        val myHandler = Handler()
        myHandler.postDelayed({

            chatMessageList.add(answer)
            mChatAdapter.notifyDataSetChanged()

        }, 1000)

        //답장 후 바닥(리스트 목록 가장 마지막)으로 리스트를 끌어내리자
        chatListView.smoothScrollToPosition(chatMessageList.size-1)

        if(strikeCount==3){

            Handler().postDelayed({
                finishGame()
            }, 1000)

        }

    }

    fun finishGame()
    {

        val congratulation = Chat("CPU", "축하합니다. 정답을 맞췄습니다.")
        chatMessageList.add(congratulation)
        mChatAdapter.notifyDataSetChanged()
        // how many times?

        val countChat = Chat("CPU", "${inputCount} 번만에 맞추었습니다.")
        chatMessageList.add(countChat)
        mChatAdapter.notifyDataSetChanged()

        //disable
        inputNumberEdt.isEnabled=false
        btnOK.isEnabled=false


        // to show message for ending
        Toast.makeText(mContext,"이용해 주셔서 감사합니다.", Toast.LENGTH_LONG).show()

    }
}
