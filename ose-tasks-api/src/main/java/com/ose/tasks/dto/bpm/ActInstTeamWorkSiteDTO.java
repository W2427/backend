package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class ActInstTeamWorkSiteDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "流程id集合,BTKDJ434")
    private List<Long> actInstIds;

    @Schema(description = "工作班组")
    private List<TeamDTO> teams;

    @Schema(description = "工作场地")
    private List<WorkSiteDTO> workSites;

    public List<Long> getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(List<Long> actInstIds) {
        this.actInstIds = actInstIds;
    }

    public class TeamDTO {

        private Long id;
        private String name;

        public TeamDTO(Long id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public class WorkSiteDTO {

        private Long id;
        private String name;

        public WorkSiteDTO(Long id, String name) {
            super();
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }

    public List<WorkSiteDTO> getWorkSites() {
        return workSites;
    }

    public void setWorkSites(List<WorkSiteDTO> workSites) {
        this.workSites = workSites;
    }
}
