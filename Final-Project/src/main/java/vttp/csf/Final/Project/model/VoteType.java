package vttp.csf.Final.Project.model;

import vttp.csf.Final.Project.exception.HomeworkNerdException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1);

    private int direction;

    VoteType(int direction) {
        this.direction = direction;
    }

    public static VoteType lookup(Integer direction) {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new HomeworkNerdException("Vote not found"));
    }

    public Integer getDirection() {
        return direction;
    }

}
