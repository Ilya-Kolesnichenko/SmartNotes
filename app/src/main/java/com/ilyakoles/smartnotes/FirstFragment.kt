package com.ilyakoles.smartnotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ilyakoles.smartnotes.databinding.FragmentFirstBinding
import java.sql.DriverManager
import java.sql.SQLException

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.button.setOnClickListener{
            Thread(Runnable {
                val sql = "SELECT * FROM users"
                mysqlConnection()
            }).start()
        }
    }

    private fun mysqlConnection() {
        println("--------------")
        try {
            Class.forName("com.mysql.jdbc.Driver")
        } catch (e: Exception) {
            println("Cannot create connection")
        }

        var retry = 0
        do {
            try {
                val conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/smartnotes",
                    "root",
                    ""
                )
                println("Connect successful")


            } catch (ex: SQLException) {
                // handle any errors
                println(ex.toString())
                println("SQLException: " + ex.message);
                println("SQLState: " + ex.sqlState);
                println("VendorError: " + ex.errorCode);
                if (ex.sqlState == "08S01") retry++
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        } while (retry <= 5)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}