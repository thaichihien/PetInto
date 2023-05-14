package com.mobye.petinto.ui.fragments

import android.R.attr.bitmap
import android.app.AlertDialog
import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentBookingDetailSpaBinding
import com.mobye.petinto.models.apimodel.Booking
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel

class BookingDetailSpaFragment : Fragment(R.layout.fragment_booking_detail_spa) {

    private var _binding : FragmentBookingDetailSpaBinding? = null
    private val binding get() = _binding!!

    private val args : BookingDetailSpaFragmentArgs by navArgs()
    private lateinit var booking : Booking

    private val serviceViewModel : ServiceViewModel by activityViewModels {
        PetIntoViewModelFactory(ServiceRepository())
    }

    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.dialog
    }

    private val warningCancelDialog : AlertDialog by lazy {
        Utils.createConfirmDialog(requireContext(),getString(R.string.cancel),getString(R.string.confirm_cancel_booking)){
            cancelBooking()
        }
    }

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

        binding.apply {

            // TODO remove border
            ivBookingID.setImageBitmap(generateQR())
            tvBookingID.text = booking.id
            lbService.text = if(booking.service == "Spa") getString(R.string.booking_detail_spa_services) else getString(R.string.booking_detail_hotel_room)
            tvService.text = booking.type
            tvStatus.text = booking.status
            tvCost.text = Utils.formatMoneyVND(booking.charge)
            tvPayment.text = booking.payment
            tvNote.text = booking.note

            // TODO Fix checkin
            tvCheckIn.text = Utils.formatToLocalDate(booking.checkIn)
            if(booking.service == "Spa"){

                trCheckout.visibility = View.GONE
            }else{
                tvCheckOut.text = Utils.formatToLocalDate(booking.checkOut)
            }
            customerNameTV.text = booking.customerName
            customerPhoneNumberTV.text = booking.phone
            tvPetName.text = booking.petName
            tvPetGenre.text = booking.genre
            tvPetWeight.text = booking.weight

            btnBack.setOnClickListener {
                if(args.from == "history"){
                    findNavController().popBackStack()
                }else{
                    findNavController().popBackStack(R.id.serviceFragment,false)
                }
            }

            if(booking.status == "Done" || booking.status == "Cancelled" || booking.status == "Unaccepted"){
                btnCancel.visibility = View.GONE
            }else{
                btnCancel.setOnClickListener {
                    warningCancelDialog.show()
                }
            }
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
        return try {
            val bitmap = qrgEncoder.getBitmap(0)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    private fun cancelBooking() {
        loadingDialog.show()

        serviceViewModel.cancelBooking(booking)

        serviceViewModel.response.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            if(args.from == "history"){
                findNavController().popBackStack()
            }else{
                findNavController().popBackStack(R.id.serviceFragment,false)
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(args.from == "history"){
                    findNavController().popBackStack()
                }else{
                    findNavController().popBackStack(R.id.serviceFragment,false)
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }
}