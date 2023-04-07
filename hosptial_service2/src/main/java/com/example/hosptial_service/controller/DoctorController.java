package com.example.hosptial_service.controller;

import com.example.hosptial_service.entity.Doctor;
import com.example.hosptial_service.entity.PatientRecord;
import com.example.hosptial_service.payloads.Consent;
import com.example.hosptial_service.payloads.HospitalAddrRequest;
import com.example.hosptial_service.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api/v1/hospital-doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @Autowired private WebClient webClient;
    @Value("${hospital.id}")
    private String hospital_id;

    @Value("${server.port}")
    private Integer hospital_addr;

    @Value("${hospital-manager.address}")
    private String hospital_manager;
    private HashMap<String, String> convert(String res) {
        HashMap<String, String> map = new HashMap<>();
        map.put("response", res);
        return map;
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create-consent")
    public ResponseEntity<?>create_consent(@RequestBody Consent consent){
        String response = "saved";
        webClient.post().uri(hospital_manager+"/hospital-addr/create-consent").bodyValue(consent).retrieve().bodyToMono(Consent.class).block();
        return ResponseEntity.accepted().body(convert(response));
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping ("/get-consent/{id}")
    public ResponseEntity<?>get_all_consents(@PathVariable Integer id) {
        List<Consent>s_list = webClient.get().uri(hospital_manager+"/hospital-addr/doctor/get-consents/"+id+"/"+hospital_id).retrieve().bodyToFlux(Consent.class).collectList().block();
        return ResponseEntity.accepted().body(s_list);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping ("/get-patient-data/{consent_id}")
    public ResponseEntity<?>get_patient_data(@PathVariable String consent_id) {
        List<PatientRecord>pr_list = webClient.get().uri(hospital_manager+"/hospital-addr/get-patient-records/"+consent_id).retrieve().bodyToFlux(PatientRecord.class).collectList().block();
        return ResponseEntity.accepted().body(pr_list);
    }


}