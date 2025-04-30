package com.ose.docs.controller.sortdata;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.domain.model.repository.*;
import com.ose.docs.domain.model.repository.project.*;
import com.ose.docs.entity.FileBasicViewES;
import com.ose.docs.entity.FileDetailES;
import com.ose.docs.entity.project.*;
import com.ose.response.JsonListResponseBody;
import com.ose.util.CryptoUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "文档接口")
@RestController
public class TestController extends BaseController {

    // 受保护文件路径
    @Value("${application.files.protected}")
    private String protectedDir;

    // 文件服务
    private final ProjectDocumentESRepository projectDocumentESRepository;

    private final FileESRepository fileESRepository;

    private final HierarchyESRepository hierarchyESRepository;

    private final PlanESRepository planESRepository;

    private final ProjectModuleProcessDefinitionRepository projectModuleProcessDefinitionRepository;

    private final WBSEntitiesESRepository wbsEntitiesESRepository;

    private final ExperienceAttachmentRepository experienceAttachmentRepository;

    private final IssueAttachmentRepository issueAttachmentRepository;

    private final IssueImportESRepository issueImportESRepository;

    private final MaterialCodeAliasGroupESRepository materialCodeAliasGroupESRepository;

    private final MaterialStructureESRepository materialStructureESRepository;

    private final NdtInspectorCertificateESRepository ndtInspectorCertificateESRepository;

    private final RatedTimeESRepository ratedTimeESRepository;

    private final SubConAttachmentESRepository subConAttachmentESRepository;

    private final WelderESRepository welderESRepository;

    private final WpqrAttachmentESRepository wpqrAttachmentESRepository;

    private final WpsAttachmentESRepository wpsAttachmentESRepository;

    private final WpsESRepository wpsESRepository;


    /**
     * 构造方法。
     */
    public TestController(
        ProjectDocumentESRepository projectDocumentESRepository,
        FileESRepository fileESRepository,
        HierarchyESRepository hierarchyESRepository,
        PlanESRepository planESRepository,
        ProjectModuleProcessDefinitionRepository projectModuleProcessDefinitionRepository,
        WBSEntitiesESRepository wbsEntitiesESRepository,
        ExperienceAttachmentRepository experienceAttachmentRepository,
        IssueAttachmentRepository issueAttachmentRepository,
        IssueImportESRepository issueImportESRepository,
        MaterialCodeAliasGroupESRepository materialCodeAliasGroupESRepository,
        MaterialStructureESRepository materialStructureESRepository,
        NdtInspectorCertificateESRepository ndtInspectorCertificateESRepository,
        RatedTimeESRepository ratedTimeESRepository,
        SubConAttachmentESRepository subConAttachmentESRepository,
        WelderESRepository welderESRepository,
        WpqrAttachmentESRepository wpqrAttachmentESRepository,
        WpsAttachmentESRepository wpsAttachmentESRepository,
        WpsESRepository wpsESRepository) {
        this.projectDocumentESRepository = projectDocumentESRepository;
        this.fileESRepository = fileESRepository;
        this.hierarchyESRepository = hierarchyESRepository;
        this.planESRepository = planESRepository;
        this.projectModuleProcessDefinitionRepository = projectModuleProcessDefinitionRepository;
        this.wbsEntitiesESRepository = wbsEntitiesESRepository;
        this.experienceAttachmentRepository = experienceAttachmentRepository;
        this.issueAttachmentRepository = issueAttachmentRepository;
        this.issueImportESRepository = issueImportESRepository;
        this.materialCodeAliasGroupESRepository = materialCodeAliasGroupESRepository;
        this.materialStructureESRepository = materialStructureESRepository;
        this.ndtInspectorCertificateESRepository = ndtInspectorCertificateESRepository;
        this.ratedTimeESRepository = ratedTimeESRepository;
        this.subConAttachmentESRepository = subConAttachmentESRepository;
        this.welderESRepository = welderESRepository;
        this.wpqrAttachmentESRepository = wpqrAttachmentESRepository;
        this.wpsAttachmentESRepository = wpsAttachmentESRepository;
        this.wpsESRepository = wpsESRepository;
    }

    @Operation(description = "查询文件")
    @RequestMapping(
        method = GET,
        value = "/sort-data",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<FileBasicViewES> sortData() {


        Iterable<ProjectDocumentES> pdESs = projectDocumentESRepository.findAll();
        pdESs.forEach(pdES -> {
            if(pdES.getId().substring(0,1).equalsIgnoreCase("B")) {
                System.out.println(pdES.getId());
                projectDocumentESRepository.delete(pdES);
            }
            pdES = getES(pdES);
            projectDocumentESRepository.save(pdES);
        });

        //
        Iterable<HierarchyES> hESs = hierarchyESRepository.findAll();
        hESs.forEach(pdES -> {
            pdES = getES(pdES);
            hierarchyESRepository.save(pdES);
        });


        Iterable<PlanES> pESs = planESRepository.findAll();
        pESs.forEach(pdES -> {
            pdES = getES(pdES);

            planESRepository.save(pdES);
        });

        projectModuleProcessDefinitionRepository.findAll()
        .forEach(pdES -> {
            pdES = getES(pdES);
            projectModuleProcessDefinitionRepository.save(pdES);
        });


         wbsEntitiesESRepository.findAll()
             .forEach(pdES -> {
                 pdES = getES(pdES);
                 wbsEntitiesESRepository.save(pdES);
             });

        experienceAttachmentRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                experienceAttachmentRepository.save(pdES);
            });

        issueAttachmentRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                issueAttachmentRepository.save(pdES);
            });

        issueImportESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                issueImportESRepository.save(pdES);
            });

        materialCodeAliasGroupESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                materialCodeAliasGroupESRepository.save(pdES);
            });;

        materialStructureESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                materialStructureESRepository.save(pdES);
            });

        ndtInspectorCertificateESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                ndtInspectorCertificateESRepository.save(pdES);
            });

        ratedTimeESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                ratedTimeESRepository.save(pdES);
            });

        subConAttachmentESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                subConAttachmentESRepository.save(pdES);
            });

        welderESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                welderESRepository.save(pdES);
            });

        wpqrAttachmentESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                wpqrAttachmentESRepository.save(pdES);
            });

        wpsAttachmentESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                wpsAttachmentESRepository.save(pdES);
            });

        wpsESRepository.findAll()
            .forEach(pdES -> {
                pdES = getDetailES(pdES);
                wpsESRepository.save(pdES);
            });

        System.out.println("out");

        return new JsonListResponseBody<>();
    }

    private <T extends ProjectBaseES> T getES(T baseES){
            String oldProjectId = baseES.getProjectId();
            if(oldProjectId != null && oldProjectId.substring(0,1).equalsIgnoreCase("B")) {
                baseES.setProjectId(CryptoUtils.th36To10(oldProjectId.substring(0,12)).toString());
            }

            String oldOrgId = baseES.getOrgId();
            if(oldOrgId != null && oldOrgId.substring(0,1).equalsIgnoreCase("B")) {
                baseES.setOrgId(CryptoUtils.th36To10(oldOrgId.substring(0,12)).toString());
            }

            String oldCommittedBy = baseES.getCommittedBy();
            if(oldCommittedBy != null && oldCommittedBy.substring(0,1).equalsIgnoreCase("B")) {
                baseES.setCommittedBy(CryptoUtils.th36To10(oldCommittedBy.substring(0,12)).toString());
            }

            String oldId = baseES.getId();
            if(oldId != null && oldId.substring(0,1).equalsIgnoreCase("B")) {
                baseES.setId(CryptoUtils.th36To10(oldId.substring(0,12)).toString());
                System.out.println(baseES.getId());
            }
        return baseES;
    }

    private <T extends FileDetailES> T getDetailES(T baseES){
        String oldOrgId = baseES.getOrgId();
        if(oldOrgId != null && oldOrgId.substring(0,1).equalsIgnoreCase("B")) {
            baseES.setOrgId(CryptoUtils.th36To10(oldOrgId.substring(0,12)).toString());
        }

        String oldCommittedBy = baseES.getCommittedBy();
        if(oldCommittedBy != null && oldCommittedBy.substring(0,1).equalsIgnoreCase("B")) {
            baseES.setCommittedBy(CryptoUtils.th36To10(oldCommittedBy.substring(0,12)).toString());
        }

        String oldId = baseES.getId();
        if(oldId != null && oldId.substring(0,1).equalsIgnoreCase("B")) {
            baseES.setId(CryptoUtils.th36To10(oldId.substring(0,12)).toString());
            System.out.println(baseES.getId());
        }
        return baseES;
    }
}
