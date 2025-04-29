package com.timebank.helpservice.help_trading.domain.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHelpTrading is a Querydsl query type for HelpTrading
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHelpTrading extends EntityPathBase<HelpTrading> {

    private static final long serialVersionUID = 592946250L;

    public static final QHelpTrading helpTrading = new QHelpTrading("helpTrading");

    public final com.timebank.common.domain.QTimestamped _super = new com.timebank.common.domain.QTimestamped(this);

    public final NumberPath<Integer> actualPoints = createNumber("actualPoints", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deletedAt = _super.deletedAt;

    //inherited
    public final StringPath deletedBy = _super.deletedBy;

    public final DateTimePath<java.time.LocalDateTime> finishedAt = createDateTime("finishedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> helperId = createNumber("helperId", Long.class);

    public final NumberPath<Long> helpRequestId = createNumber("helpRequestId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> requesterId = createNumber("requesterId", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final EnumPath<com.timebank.helpservice.help_trading.domain.TradeStatus> tradeStatus = createEnum("tradeStatus", com.timebank.helpservice.help_trading.domain.TradeStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    public QHelpTrading(String variable) {
        super(HelpTrading.class, forVariable(variable));
    }

    public QHelpTrading(Path<? extends HelpTrading> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHelpTrading(PathMetadata metadata) {
        super(HelpTrading.class, metadata);
    }

}

