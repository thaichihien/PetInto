package com.mobye.petinto.ui.fragments

import android.R.attr.bitmap
import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentBookingDetailSpaBinding
import com.mobye.petinto.models.apimodel.Booking
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Utils


class BookingDetailSpaFragment : Fragment(R.layout.fragment_booking_detail_spa) {

    private var _binding : FragmentBookingDetailSpaBinding? = null
    private val binding get() = _binding!!

    private val args : BookingDetailSpaFragmentArgs by navArgs()
    private lateinit var booking : Booking
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingDetailSpaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        booking = args.booking
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.hideBottomNav()

        binding.apply {

            // TODO remove border
            ivBookingID.setImageBitmap(generateQR())
            tvBookingID.text = booking.id
            lbService.text = if(booking.service == "Spa") "Service:" else "Room:"
            tvService.text = booking.type
            tvStatus.text = booking.status
            tvCost.text = "%,d đ".format(booking.charge)
            // TODO Fix checkin
            tvCheckIn.text = Utils.formatToLocalDate(booking.checkIn)
            if(booking.service == "Spa"){
                tvCheckOut.visibility = View.GONE
            }else{
                tvCheckOut.text = booking.checkOut
            }
            customerNameTV.text = booking.customerName
            customerPhoneNumberTV.text = booking.phone
            tvPetName.text = booking.petName
            tvPetGenre.text = booking.genre
            tvPetWeight.text = booking.weight

            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }

            //TODO handle Cancel button
        }




    }

    private fun generateQR() : Bitmap? {
        val manager = requireActivity().getSystemService(WINDOW_SERVICE) as WindowManager
        val display = manager.currentWindowMetrics

        val point = display.bounds
        val width: Int = point.width()
        val height: Int = point.height()
        var smallerDimension = width.coerceAtMost(height)
        smallerDimension = smallerDimension * 3 / 4

        val qrgEncoder = QRGEncoder(booking.id, QRGContents.Type.TEXT, smallerDimension)
        qrgEncoder.colorBlack = Color.BLACK
        qrgEncoder.colorWhite = Color.WHITE
        try {
            val bitmap = qrgEncoder.bitmap
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }



    }




}