package vn.thecode.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.thecode.jobhunter.domain.Company;
import vn.thecode.jobhunter.domain.Skill;
import vn.thecode.jobhunter.domain.response.ResultPaginationDTO;
import vn.thecode.jobhunter.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill fetchSkillById(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        if (skillOptional.isPresent()) {
            return skillOptional.get();
        }
        return null;
    }

    public Skill createSkill(Skill s) {
        return this.skillRepository.save(s);
    }

    public Skill updateSkill(Skill s) {
        return this.skillRepository.save(s);
    }

    public ResultPaginationDTO fetchAllSkill(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> page = this.skillRepository.findAll(specification, pageable);
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageNumber());

        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getNumberOfElements());

        paginationDTO.setMeta(mt);
        paginationDTO.setResult(page.getContent());

        return paginationDTO;
    }

    public void deleteSkill(long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        this.skillRepository.delete(currentSkill);
    }
}
