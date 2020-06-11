package com.example.numberbaseballgame.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.numberbaseballgame.R
import com.example.numberbaseballgame.datas.Chat
import org.w3c.dom.Text

class ChatAdapter(val mContext: Context, val resId:Int, val mList:List<Chat>) : ArrayAdapter<Chat>(mContext, resId, mList) {
    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        tempRow.let {

        }.let {
            tempRow = inf.inflate(R.layout.chat_list_item,null)
        }
        val row = tempRow!!

       
        val chatComputerLayout = row.findViewById<LinearLayout>(R.id.computerChatLayout)
        val chatUserLayout = row.findViewById<LinearLayout>(R.id.userChatLayout)

        val computerChatTxt = row.findViewById<TextView>(R.id.computerChatTxt)
        val userChatTxt = row.findViewById<TextView>(R.id.userChatTxt)

        val data = mList[position]
        // 컴퓨터가 말할 때와 사람이 말할 때를 구분하여 표시
        when(data.who){
            "USER" ->{
                chatUserLayout.visibility = View.VISIBLE
                chatComputerLayout.visibility =View.GONE

                userChatTxt.text = data.content
                
            }
            "CPU" -> {

                chatUserLayout.visibility = View.GONE
                chatComputerLayout.visibility =View.VISIBLE

                computerChatTxt.text = data.content
            }
            else -> {
                Log.e("에러발생","사용자/컴퓨터와의 화자 생김")
            }
            
        }
        

        return row
    }
}