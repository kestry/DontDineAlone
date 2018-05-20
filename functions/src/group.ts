

export class Group {
    private gid: number;
    private status: string;
    private groupSizePreferences: Boolean[];
    private diningHallPreferences: Boolean[];
    private members: Map<string, Boolean>;

    public getGid() {
        return this.gid;
    }
    public getStatus() {
        return this.status;
    }
    public getGroupSizePreferences() {
        return this.groupSizePreferences;
    }
    public getDiningHallPreferences() {
        return this.diningHallPreferences;
    }
    public getMembers() {
        return this.members;
    }
    public setGid(gid: number) {
        this.gid = gid;
    }
    public setGroupSizePreferences(groupSizePreferences: Boolean[]) {
        this.groupSizePreferences = groupSizePreferences;
    }
    public setDiningHallPreferences(diningHallPreferences: Boolean[]) {
        this.groupSizePreferences = diningHallPreferences;
    }
    public setMembers(members: Map<string, Boolean) {
        this.members = members;
    }
}