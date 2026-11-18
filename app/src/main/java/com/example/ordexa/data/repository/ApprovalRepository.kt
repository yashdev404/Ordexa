package com.example.ordexa.data.repository

import com.example.ordexa.data.mock.MockData
import com.example.ordexa.domain.model.ApprovalStatus
import com.example.ordexa.domain.model.EodShiftReport
import com.example.ordexa.domain.model.PendingApprovalItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ApprovalRepository {
    private val _pendingApprovals = MutableStateFlow(MockData.samplePendingApprovals)
    val pendingApprovals: Flow<List<PendingApprovalItem>> = _pendingApprovals.asStateFlow()

    private val _eodReport = MutableStateFlow(MockData.sampleEodReport)
    val eodReport: Flow<EodShiftReport> = _eodReport.asStateFlow()

    fun approveItem(id: String, approverName: String, notes: String? = null): Boolean {
        _pendingApprovals.value = _pendingApprovals.value.map { item ->
            if (item.id == id) {
                item.copy(
                    status = ApprovalStatus.APPROVED,
                    approvedBy = approverName,
                    actionNotes = notes ?: "Approved by Authority"
                )
            } else item
        }.toMutableList()
        updatePendingCount()
        return true
    }

    fun rejectItem(id: String, approverName: String, notes: String? = null): Boolean {
        _pendingApprovals.value = _pendingApprovals.value.map { item ->
            if (item.id == id) {
                item.copy(
                    status = ApprovalStatus.REJECTED,
                    approvedBy = approverName,
                    actionNotes = notes ?: "Rejected by Authority"
                )
            } else item
        }.toMutableList()
        updatePendingCount()
        return true
    }

    fun sealEodShift(managerName: String): Boolean {
        val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val current = _eodReport.value
        _eodReport.value = current.copy(
            isSealed = true,
            sealedAt = now,
            sealedBy = managerName
        )
        return true
    }

    private fun updatePendingCount() {
        val pendingCount = _pendingApprovals.value.count { it.status == ApprovalStatus.PENDING }
        _eodReport.value = _eodReport.value.copy(pendingApprovalsCount = pendingCount)
    }
}
