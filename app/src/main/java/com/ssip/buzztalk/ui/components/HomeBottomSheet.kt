package com.ssip.buzztalk.ui.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ssip.buzztalk.databinding.FilterLayoutBinding
import com.ssip.buzztalk.utils.BottomSheetOptions
import com.ssip.buzztalk.utils.BottomSheetOptions.ENTREPRENEURS
import com.ssip.buzztalk.utils.BottomSheetOptions.INDUSTRY
import com.ssip.buzztalk.utils.BottomSheetOptions.PROFESSORS
import com.ssip.buzztalk.utils.BottomSheetOptions.RESEARCH_SCHOLARS
import com.ssip.buzztalk.utils.BottomSheetOptions.SCHOOL
import com.ssip.buzztalk.utils.BottomSheetOptions.STUDENTS
import com.ssip.buzztalk.utils.BottomSheetOptions.UNIVERSITY
import com.ssip.buzztalk.utils.BottomSheetOptions.WORKING_PROFESSIONAL

class HomeBottomSheet: BottomSheetDialogFragment() {
    private var _binding: FilterLayoutBinding? = null
    private val binding get() = _binding!!

    private val homeBottomSheetViewModel: HomeBottomSheetViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FilterLayoutBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    with(binding) {
      for (chips in chipGroup.checkedChipIds) {
        if (industry.id == chips) {
          homeBottomSheetViewModel.addData(INDUSTRY)
        }
        if (researchScholars.id == chips) {
          homeBottomSheetViewModel.addData(RESEARCH_SCHOLARS)
        }
        if (school.id == chips) {
          homeBottomSheetViewModel.addData(SCHOOL)
        }
        if (workingProfessional.id == chips) {
          homeBottomSheetViewModel.addData(WORKING_PROFESSIONAL)
        }
        if (universities.id == chips) {
          homeBottomSheetViewModel.addData(UNIVERSITY)
        }
        if (professors.id == chips) {
          homeBottomSheetViewModel.addData(PROFESSORS)
        }
        if (student.id == chips) {
          homeBottomSheetViewModel.addData(STUDENTS)
        }
        if (entrepreneurs.id == chips) {
          homeBottomSheetViewModel.addData(ENTREPRENEURS)
        }
      }
      btnApply.setOnClickListener {
        val cat = "${chipGroup.checkedChipIds.size}"
        Toast.makeText(context, cat, Toast.LENGTH_SHORT).show()
      }
    }
  }
}