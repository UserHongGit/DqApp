package com.hong.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hong.dao.LocalRepo;
import com.hong.dao.TraceRepo;
import com.hong.mvp.model.filter.TrendingSince;
import java.util.Date;

public class Repository implements Parcelable {
    public static final Creator<Repository> CREATOR = new Creator<Repository>() {
        public Repository createFromParcel(Parcel source) {
            return new Repository(source);
        }

        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };
    private String avatarurl;
    private String banbaoType;
    @SerializedName("clone_url")
    private String cloneUrl;
    private String completeJudgement;
    private Date createTime;
    @SerializedName("created_at")
    private Date createdAt;
    private String dailyid;
    @SerializedName("default_branch")
    private String defaultBranch;
    private String description;
    private boolean fork;
    @SerializedName("forks_count")
    private int forksCount;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("git_url")
    private String gitUrl;
    @SerializedName("has_downloads")
    private boolean hasDownloads;
    @SerializedName("has_issues")
    private boolean hasIssues;
    @SerializedName("has_pages")
    private boolean hasPages;
    @SerializedName("has_projects")
    private boolean hasProjects;
    @SerializedName("has_wiki")
    private boolean hasWiki;
    @SerializedName("html_url")
    private String htmlUrl;
    private int id;
    private String intelligenceCode;
    private String language;
    private String name;
    @SerializedName("open_issues_count")
    private int openIssuesCount;
    private String orderClasses;
    private User owner;
    private Repository parent;
    private RepositoryPermissions permissions;
    @SerializedName("pushed_at")
    private Date pushedAt;
    @SerializedName("private")
    private boolean repPrivate;
    private Date reportTime;
    private TrendingSince since;
    private int sinceStargazersCount;
    private long size;
    @SerializedName("ssh_url")
    private String sshUrl;
    @SerializedName("stargazers_count")
    private int stargazersCount;
    @SerializedName("subscribers_count")
    private int subscribersCount;
    @SerializedName("svn_url")
    private String svnUrl;
    private String teamName;
    private String title;
    @SerializedName("updated_at")
    private Date updatedAt;
    private int visittimes;
    @SerializedName("watchers_count")
    private int watchersCount;
    private String wellCommonName;
    private String workBrief;
    private Date workDate;

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVisittimes() {
        return this.visittimes;
    }

    public void setVisittimes(int visittimes) {
        this.visittimes = visittimes;
    }

    public String getAvatarurl() {
        return this.avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public LocalRepo toLocalRepo() {
        LocalRepo localRepo = new LocalRepo();
        localRepo.setId((long) this.id);
        localRepo.setName(this.name);
        localRepo.setDescription(this.description);
        localRepo.setDailyid(this.dailyid);
        localRepo.setAvatarurl(this.avatarurl);
        localRepo.setTitle(this.title);
        localRepo.setLanguage(this.language);
        localRepo.setStargazersCount(Integer.valueOf(this.stargazersCount));
        localRepo.setWatchersCount(Integer.valueOf(this.watchersCount));
        localRepo.setForksCount(Integer.valueOf(this.forksCount));
        localRepo.setFork(Boolean.valueOf(this.fork));
        localRepo.setOwnerLogin(this.owner.getLogin());
        localRepo.setOwnerAvatarUrl(this.owner.getAvatarUrl());
        localRepo.setWellCommonName(this.wellCommonName);
        localRepo.setIntelligenceCode(this.intelligenceCode);
        localRepo.setWorkBrief(this.workBrief);
        localRepo.setTeamName(this.teamName);
        localRepo.setReportTime(this.reportTime);
        localRepo.setWorkDate(this.workDate);
        localRepo.setOrderClasses(this.orderClasses);
        localRepo.setBanbaoType(this.banbaoType);
        localRepo.setCompleteJudgement(this.completeJudgement);
        return localRepo;
    }

    public static Repository generateFromLocalRepo(LocalRepo localRepo) {
        Repository repo = new Repository();
        repo.setId((int) localRepo.getId());
        repo.setName(localRepo.getName());
        repo.setDescription(localRepo.getDescription());
        repo.setDailyid(localRepo.getDailyid());
        repo.setTitle(localRepo.getTitle());
        repo.setAvatarurl(localRepo.getAvatarurl());
        repo.setWellCommonName(localRepo.getWellCommonName());
        repo.setIntelligenceCode(localRepo.getIntelligenceCode());
        repo.setWorkBrief(localRepo.getWorkBrief());
        repo.setTeamName(localRepo.getTeamName());
        repo.setOrderClasses(localRepo.getOrderClasses());
        repo.setCompleteJudgement(localRepo.getCompleteJudgement());
        repo.setBanbaoType(localRepo.getBanbaoType());
        repo.setReportTime(localRepo.getReportTime());
        repo.setWorkDate(localRepo.getWorkDate());
        repo.setLanguage(localRepo.getLanguage());
        repo.setStargazersCount(localRepo.getStargazersCount().intValue());
        repo.setWatchersCount(localRepo.getWatchersCount().intValue());
        repo.setForksCount(localRepo.getForksCount().intValue());
        repo.setFork(localRepo.getFork().booleanValue());
        User user = new User();
        user.setLogin(localRepo.getOwnerLogin());
        user.setAvatarUrl(localRepo.getOwnerAvatarUrl());
        repo.setOwner(user);
        return repo;
    }

    public static Repository generateFromTrace(TraceRepo trace) {
        Repository repo = new Repository();
        repo.setId((int) trace.getId());
        repo.setName(trace.getName());
        repo.setDescription(trace.getDescription());
        repo.setLanguage(trace.getLanguage());
        repo.setStargazersCount(trace.getStargazersCount().intValue());
        repo.setWatchersCount(trace.getWatchersCount().intValue());
        repo.setForksCount(trace.getForksCount().intValue());
        repo.setFork(trace.getFork().booleanValue());
        User user = new User();
        user.setLogin(trace.getOwnerLogin());
        user.setAvatarUrl(trace.getOwnerAvatarUrl());
        repo.setOwner(user);
        return repo;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isRepPrivate() {
        return this.repPrivate;
    }

    public void setRepPrivate(boolean repPrivate) {
        this.repPrivate = repPrivate;
    }

    public String getHtmlUrl() {
        return this.htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getDescription() {
        return this.description;
    }

    public String getIntelligenceCode() {
        return this.intelligenceCode;
    }

    public void setIntelligenceCode(String intelligenceCode) {
        this.intelligenceCode = intelligenceCode;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getDailyid() {
        return this.dailyid;
    }

    public String getWorkBrief() {
        return this.workBrief;
    }

    public void setWorkBrief(String workBrief) {
        this.workBrief = workBrief;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getBanbaoType() {
        return this.banbaoType;
    }

    public void setBanbaoType(String banbaoType) {
        this.banbaoType = banbaoType;
    }

    public String getCompleteJudgement() {
        return this.completeJudgement;
    }

    public void setCompleteJudgement(String completeJudgement) {
        this.completeJudgement = completeJudgement;
    }

    public String getOrderClasses() {
        return this.orderClasses;
    }

    public void setOrderClasses(String orderClasses) {
        this.orderClasses = orderClasses;
    }

    public Date getReportTime() {
        return this.reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Date getWorkDate() {
        return this.workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public void setDailyid(String dailyid) {
        this.dailyid = dailyid;
    }

    public String getWellCommonName() {
        return this.wellCommonName;
    }

    public void setWellCommonName(String wellCommonName) {
        this.wellCommonName = wellCommonName;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getDefaultBranch() {
        return this.defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getPushedAt() {
        return this.pushedAt;
    }

    public void setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
    }

    public String getGitUrl() {
        return this.gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getSshUrl() {
        return this.sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getCloneUrl() {
        return this.cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getSvnUrl() {
        return this.svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getStargazersCount() {
        return this.stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public int getWatchersCount() {
        return this.watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public int getForksCount() {
        return this.forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public int getOpenIssuesCount() {
        return this.openIssuesCount;
    }

    public void setOpenIssuesCount(int openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public boolean isFork() {
        return this.fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public RepositoryPermissions getPermissions() {
        return this.permissions;
    }

    public void setPermissions(RepositoryPermissions permissions) {
        this.permissions = permissions;
    }

    public int getSubscribersCount() {
        return this.subscribersCount;
    }

    public void setSubscribersCount(int subscribersCount) {
        this.subscribersCount = subscribersCount;
    }

    public Repository getParent() {
        return this.parent;
    }

    public void setParent(Repository parent) {
        this.parent = parent;
    }

    public boolean isHasIssues() {
        return this.hasIssues;
    }

    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    public boolean isHasProjects() {
        return this.hasProjects;
    }

    public void setHasProjects(boolean hasProjects) {
        this.hasProjects = hasProjects;
    }

    public boolean isHasDownloads() {
        return this.hasDownloads;
    }

    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    public boolean isHasWiki() {
        return this.hasWiki;
    }

    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    public boolean isHasPages() {
        return this.hasPages;
    }

    public void setHasPages(boolean hasPages) {
        this.hasPages = hasPages;
    }

    public int getSinceStargazersCount() {
        return this.sinceStargazersCount;
    }

    public void setSinceStargazersCount(int sinceStargazersCount) {
        this.sinceStargazersCount = sinceStargazersCount;
    }

    public TrendingSince getSince() {
        return this.since;
    }

    public void setSince(TrendingSince since) {
        this.since = since;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeByte(this.repPrivate ? (byte) 1 : (byte) 0);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.description);
        dest.writeString(this.avatarurl);
        dest.writeString(this.title);
        dest.writeString(this.dailyid);
        dest.writeString(this.language);
        dest.writeParcelable(this.owner, flags);
        dest.writeString(this.defaultBranch);
        Date date = this.createdAt;
        long j = -1;
        dest.writeLong(date != null ? date.getTime() : -1);
        date = this.updatedAt;
        dest.writeLong(date != null ? date.getTime() : -1);
        date = this.pushedAt;
        if (date != null) {
            j = date.getTime();
        }
        dest.writeLong(j);
        dest.writeString(this.gitUrl);
        dest.writeString(this.sshUrl);
        dest.writeString(this.cloneUrl);
        dest.writeString(this.svnUrl);
        dest.writeLong(this.size);
        dest.writeInt(this.stargazersCount);
        dest.writeInt(this.watchersCount);
        dest.writeInt(this.forksCount);
        dest.writeInt(this.openIssuesCount);
        dest.writeInt(this.subscribersCount);
        dest.writeByte(this.fork ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.parent, flags);
        dest.writeParcelable(this.permissions, flags);
        dest.writeByte(this.hasIssues ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasProjects ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasDownloads ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasWiki ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasPages ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sinceStargazersCount);
        TrendingSince trendingSince = this.since;
        dest.writeInt(trendingSince == null ? -1 : trendingSince.ordinal());
    }

    public Repository() {
    }

    protected Repository(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.fullName = in.readString();
        boolean z = true;
        this.repPrivate = in.readByte() != (byte) 0;
        this.htmlUrl = in.readString();
        this.description = in.readString();
        this.avatarurl = in.readString();
        this.title = in.readString();
        this.dailyid = in.readString();
        this.language = in.readString();
        this.owner = (User) in.readParcelable(User.class.getClassLoader());
        this.defaultBranch = in.readString();
        long tmpCreatedAt = in.readLong();
        TrendingSince trendingSince = null;
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        long tmpPushedAt = in.readLong();
        this.pushedAt = tmpPushedAt == -1 ? null : new Date(tmpPushedAt);
        this.gitUrl = in.readString();
        this.sshUrl = in.readString();
        this.cloneUrl = in.readString();
        this.svnUrl = in.readString();
        this.size = in.readLong();
        this.stargazersCount = in.readInt();
        this.watchersCount = in.readInt();
        this.forksCount = in.readInt();
        this.openIssuesCount = in.readInt();
        this.subscribersCount = in.readInt();
        this.fork = in.readByte() != (byte) 0;
        this.parent = (Repository) in.readParcelable(Repository.class.getClassLoader());
        this.permissions = (RepositoryPermissions) in.readParcelable(RepositoryPermissions.class.getClassLoader());
        this.hasIssues = in.readByte() != (byte) 0;
        this.hasProjects = in.readByte() != (byte) 0;
        this.hasDownloads = in.readByte() != (byte) 0;
        this.hasWiki = in.readByte() != (byte) 0;
        if (in.readByte() == (byte) 0) {
            z = false;
        }
        this.hasPages = z;
        this.sinceStargazersCount = in.readInt();
        int tmpTrendingSince = in.readInt();
        if (tmpTrendingSince != -1) {
            trendingSince = TrendingSince.values()[tmpTrendingSince];
        }
        this.since = trendingSince;
    }
}
