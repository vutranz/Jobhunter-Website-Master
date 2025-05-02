package vn.thecode.jobhunter.controller;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.thecode.jobhunter.domain.Company;
import vn.thecode.jobhunter.domain.response.ResultPaginationDTO;
import vn.thecode.jobhunter.service.CompanyService;
import vn.thecode.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("api/${api.version}")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanys(
            @Filter Specification<Company> spec,
            Pageable pageable) {

        ResultPaginationDTO companies = companyService.handleFindAllCompanies(spec, pageable);
        ResponseEntity<ResultPaginationDTO> responseEntity = ResponseEntity
                .status(HttpStatus.OK)
                .body(companies);
        return responseEntity;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company company) {

        Company newCompany = this.companyService.handleSaveCompany(company);
        ResponseEntity<Company> responseEntity = ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newCompany);
        return responseEntity;
    }

    @DeleteMapping("/companies/{id-company}")
    public ResponseEntity<Void> deleteCompanyById(@PathVariable("id-company") long id) throws IdInvalidException {
        this.companyService.handleRemoveCompanyById(id);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) {
        Company updatedCompany = companyService.handleModifyCompany(company);
        ResponseEntity<Company> responseEntity = ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedCompany);
        return responseEntity;
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id) {
        Optional<Company> cOptional = this.companyService.findById(id);

        return ResponseEntity.ok().body(cOptional.get());
    }

}
