package com.pransquare.nems.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.pransquare.nems.entities.EmployeePayslip;
import com.pransquare.nems.models.PayslipConfigDTO;
import com.pransquare.nems.models.PayslipResModal;
import com.pransquare.nems.repositories.EmployeePayslipRepository;

@Service
public class EmployeePayslipService {

    private final EmployeePayslipRepository repository;
    private final RestTemplate restTemplate;

    @Value("${master-config-service.url}")
    private String masterConfigServiceUrl;

    public EmployeePayslipService(EmployeePayslipRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    public List<EmployeePayslip> createPayslip(List<EmployeePayslip> payslip) {
        return repository.saveAll(payslip);
    }

    public Map<String, PayslipResModal> getPayslipsByEmployeeId(Long employeeId) {
        try {
            Map<String, PayslipResModal> response = new HashMap<>();
            List<PayslipConfigDTO> payslipConfigs = new ArrayList<>();

            String url = masterConfigServiceUrl + "/Pransquare/MasterConfiguration/payslip-config/by-country/IND";

            ResponseEntity<List<PayslipConfigDTO>> payslipConfigResponse = restTemplate.exchange(url, HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<PayslipConfigDTO>>() {
                    });
            payslipConfigs = payslipConfigResponse.getBody();
            if (payslipConfigs == null) {
                return response;
            }
            List<EmployeePayslip> employeePayslips = repository.findByEmployeeId(employeeId);
            payslipConfigs.forEach(a -> {
                PayslipResModal payslipResModal = new PayslipResModal();
                EmployeePayslip employeePayslip = employeePayslips.stream()
                        .filter(b -> b.getConfigCode().equals(a.getCode())).findFirst().orElse(null);
                if (employeePayslip != null) {
                    payslipResModal = new PayslipResModal(employeePayslip.getId(), a.getAmountName(),
                            employeePayslip.getAmount(),
                            a.getCurrency(), null, a.getAmountType());
                } else {
                    payslipResModal = new PayslipResModal(null, a.getAmountName(), null, a.getCurrency(),
                            null, a.getAmountType());
                }
                response.put(a.getCode(), payslipResModal);
            });
            return response;
        } catch (RestClientException e) {
            throw new RestClientException("Error while fetching payroll configuration", e.fillInStackTrace());
        }
    }

}
