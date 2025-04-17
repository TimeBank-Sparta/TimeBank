package com.timebank.helpservice.helper.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHelper is a Querydsl query type for Helper
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHelper extends EntityPathBase<Helper> {

    private static final long serialVersionUID = -1477498033L;

    public static final QHelper helper = new QHelper("helper");

    public final com.timebank.common.domain.QTimestamped _super = new com.timebank.common.domain.QTimestamped(this);

    public final EnumPath<com.timebank.helpservice.helper.domain.ApplicantStatus> applicantStatus = createEnum("applicantStatus", com.timebank.helpservice.helper.domain.ApplicantStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final StringPath deletedBy = _super.deletedBy;

    public final NumberPath<Long> helpRequestId = createNumber("helpRequestId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QHelper(String variable) {
        super(Helper.class, forVariable(variable));
    }

    public QHelper(Path<? extends Helper> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHelper(PathMetadata metadata) {
        super(Helper.class, metadata);
    }

}

