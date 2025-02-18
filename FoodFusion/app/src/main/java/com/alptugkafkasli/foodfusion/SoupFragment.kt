package com.alptugkafkasli.foodfusion

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alptugkafkasli.foodfusion.databinding.FragmentBreakfastBinding
import com.alptugkafkasli.foodfusion.databinding.FragmentSoupBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SoupFragment : Fragment() {
    private var _binding: FragmentSoupBinding? = null
    private val binding get() = _binding!!
    private lateinit var db:FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db=Firebase.firestore
        _binding = FragmentSoupBinding.inflate(inflater, container, false)
        getData()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }
    private fun setOnClickListeners(){
        binding.soupButton.setOnClickListener {
            getData()
        }

    }
    private fun getData(){
        db.collection("Soup").get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d(TAG, "Belge bulunamadı.")
                    return@addOnSuccessListener
                }
                // Rastgele bir belge seçmek için belgelerin listesini oluşturun
                val belgeListesi = documents.toMutableList()

                // Rastgele bir belge seçin
                val rastgeleBelge = belgeListesi.random()
                val name = rastgeleBelge.getString("Name")
                val description = rastgeleBelge.getString("Description")
                val imageUrl = rastgeleBelge.getString("ImageUrl")
                val recipes = rastgeleBelge.get("Recipes") as? List<String>
                val ingredients = rastgeleBelge.get("Ingredients") as? List<String>
                binding.nameSoup.text=name.toString()
                binding.descriptionSoup.text=description.toString()

                if (!imageUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(imageUrl)
                        .into(binding.imageSoup)
                } else {
                    // imageUrl null veya boş
                    // Varsayılan bir görüntü yükleyebilir veya bir hata mesajı yazdırabilirsiniz
                    Log.e(TAG, "ImageUrl boş veya null")
                }

                if (recipes != null && recipes.isNotEmpty()) {
                    val tariflerMetin = StringBuilder()
                    for (tarif in recipes) {
                        tariflerMetin.append("-$tarif\n\n")
                    }
                    binding.recipesSoup.text = tariflerMetin.toString()
                } else {
                    println("Tarif bulunamadı veya boş.")
                }
                if (ingredients != null && ingredients.isNotEmpty()) {
                    val tariflerMetin = StringBuilder()
                    for (tarif in ingredients) {
                        tariflerMetin.append("$tarif\n\n")
                    }
                    binding.ingredientsSoup.text = tariflerMetin.toString()
                } else {
                    println("Tarif bulunamadı veya boş.")
                }


                // Rastgele belgeyi kullanın
                val belgeId = rastgeleBelge.id
                val belgeVerisi = rastgeleBelge.data

                // Rastgele belgeyi kullandıktan sonra istediğiniz işlemi yapabilirsiniz
                // Örneğin, belge verisini ekrana yazdırabilirsiniz
                Log.d(TAG, "Rastgele Belge ID: $belgeId, Veri: $belgeVerisi")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Belgeleri alma işlemi başarısız oldu.", exception)
            }
    }


}