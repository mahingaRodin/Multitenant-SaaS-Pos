package com.msp.services;

import com.msp.payloads.dtos.ShiftReportDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShiftService {
    ShiftReportDto startShift() throws Exception;
    ShiftReportDto endShift(UUID shiftReportId, LocalDateTime shiftEnd) throws Exception;
    ShiftReportDto getShiftReportById(UUID id) throws Exception;
    List<ShiftReportDto> getAllShiftReports();
    List<ShiftReportDto> getShiftReportByBranchId(UUID branchId);
    List<ShiftReportDto> getShiftReportByCashierId(UUID cashierId);
    ShiftReportDto getCurrentShiftProgress(UUID cashierId) throws Exception;
    ShiftReportDto getShiftByCashierAndDate(UUID cashierId, LocalDateTime date) throws Exception;
}
