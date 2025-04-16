package com.timebank.common.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTimestamped is a Querydsl query type for Timestamped
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QTimestamped extends EntityPathBase<Timestamped> {

    private static final long serialVersionUID = -1527098570L;

    public static final QTimestamped timestamped = new QTimestamped("timestamped");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath createdBy = createString("createdBy");

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final StringPath deletedBy = createString("deletedBy");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final StringPath updatedBy = createString("updatedBy");

    public QTimestamped(String variable) {
        super(Timestamped.class, forVariable(variable));
    }

    public QTimestamped(Path<? extends Timestamped> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTimestamped(PathMetadata metadata) {
        super(Timestamped.class, metadata);
    }

}

