package uz.abboskhan.firebaserealtime

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.abboskhan.firebaserealtime.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
   // private lateinit var firebase: FirebaseAuth
    lateinit var mList: ArrayList<CountryData>
    private lateinit var adapter: CategoryAdapter
    private var category: String = ""
    private val storageReference: StorageReference =
        FirebaseStorage.getInstance().getReference("Images")
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Data")
    private var imageUri: Uri? = null
    private lateinit var dialogImages: ImageView
    private lateinit var dialogText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataRew()
        getInit()
         fetchDataFromDatabase()

    }

    private fun getInit() {
        //firebase = FirebaseAuth.getInstance()
        binding.floatingActionButton.setOnClickListener {
            dialogData()
            // startActivity(Intent(this,AddActiviy::class.java))
        }
        binding.rewCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.floatingActionButton.isShown) {
                    binding.floatingActionButton.hide()
                } else if (dy < 0 && !binding.floatingActionButton.isShown) {
                    binding.floatingActionButton.show()
                }
            }
        })

    }

    private fun getDataRew() {
        mList = ArrayList()
//        val firebaseData = FirebaseDatabase.getInstance().getReference("Category")
//        firebaseData.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                mList.clear()
//                for (i in snapshot.children) {
//                    val data = i.getValue(CountryData::class.java)
//                    mList.add(data!!)
//                }
                adapter = CategoryAdapter(mList)
                binding.rewCategory.setHasFixedSize(true)
                binding.rewCategory.layoutManager = LinearLayoutManager(this@MainActivity)
                binding.rewCategory.adapter = adapter

//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })

    }

    @SuppressLint("MissingInflatedId")
    private fun dialogData() {
        val editDialog = AlertDialog.Builder(this)
        val editDialogView = layoutInflater.inflate(R.layout.dialog_edd, null)

        dialogText = editDialogView.findViewById<EditText>(R.id.dialog_text)
        dialogImages = editDialogView.findViewById(R.id.imageView)
        val progressBar = editDialogView.findViewById<ProgressBar>(R.id.progressBar)
        dialogImages.setOnClickListener {
            resultLauncher.launch("image/*")
        }




        editDialog.setView(editDialogView)
        editDialog.setPositiveButton("Saqlash") { _, _ ->
//            val category = dialogText.text.toString().trim()
//            progressBar.visibility = View.VISIBLE
//            val timestamp = System.currentTimeMillis()

            uploadImageToFirebase()
   /*         val hashMap = HashMap<String, Any>()
            hashMap["id"] = "$timestamp"
            hashMap["category"] = category
            hashMap["timestamp"] = timestamp


            val firebaseData = FirebaseDatabase.getInstance().getReference("Category")
            firebaseData.child("$timestamp")
                .setValue(hashMap)
                .addOnSuccessListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Malumotlar qo'shildi.", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
*/


        }
        editDialog.setNegativeButton("Bekor qilish") { _, _ ->

        }

        val dialog = editDialog.create()
        dialog.show()


    }
  private  fun uploadImageToFirebase() {
        val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")

        imageUri?.let {
            imageRef.putFile(it)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        val text = dialogText.text.toString()
                        saveDataToDatabase(imageUrl, text)
                    }
                }
                .addOnFailureListener { e ->
                    // Rasmni yuklashda xatolik yuz berdi
                }
        }
    }
    private fun saveDataToDatabase(imageUrl: String, text: String) {
        val key = databaseReference.push().key

        if (key != null) {
            val data = CountryData(imageUrl, text)
            databaseReference.child(key).setValue(data)
        }
    }



    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        dialogImages.setImageURI(it)

    }
    private fun fetchDataFromDatabase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (dataSnapshot in snapshot.children) {
                    val data = dataSnapshot.getValue(CountryData::class.java)
                    if (data != null) {
                        mList.add(data)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Ma'lumotlarni o'qishda xatolik yuz berdi
            }
        })
    }


}