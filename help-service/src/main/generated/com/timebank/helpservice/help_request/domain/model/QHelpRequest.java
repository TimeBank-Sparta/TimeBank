package com.timebank.helpservice.help_request.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHelpRequest is a Querydsl query type for HelpRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHelpRequest extends EntityPathBase<HelpRequest> {

    private static final long serialVersionUID = -1064961014L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHelpRequest helpRequest = new QHelpRequest("helpRequest");

    public final com.timebank.common.domain.QTimestamped _super = new com.timebank.common.domain.QTimestamped(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final StringPath deletedBy = _super.deletedBy;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.timebank.helpservice.help_request.domain.vo.QHelpRequestLocation location;

    public final com.timebank.helpservice.help_request.domain.vo.QHelpRequestMetrics postMetric;

    public final EnumPath<com.timebank.helpservice.help_request.domain.PostStatus> postStatus = createEnum("postStatus", com.timebank.helpservice.help_request.domain.PostStatus.class);

    public final NumberPath<Integer> recruitmentCount = createNumber("recruitmentCount", Integer.class);

    public final NumberPath<Integer> requestedPoint = createNumber("requestedPoint", Integer.class);

    public final NumberPath<Long> requesterId = createNumber("requesterId", Long.class);

    public final NumberPath<Integer> requiredTime = createNumber("requiredTime", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> scheduledAt = createDateTime("scheduledAt", java.time.LocalDateTime.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QHelpRequest(String variable) {
        this(HelpRequest.class, forVariable(variable), INITS);
    }

    public QHelpRequest(Path<? extends HelpRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHelpRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHelpRequest(PathMetadata metadata, PathInits inits) {
        this(HelpRequest.class, metadata, inits);
    }

    public QHelpRequest(Class<? extends HelpRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new com.timebank.helpservice.help_request.domain.vo.QHelpRequestLocation(forProperty("location")) : null;
        this.postMetric = inits.isInitialized("postMetric") ? new com.timebank.helpservice.help_request.domain.vo.QHelpRequestMetrics(forProperty("postMetric")) : null;
    }

}

