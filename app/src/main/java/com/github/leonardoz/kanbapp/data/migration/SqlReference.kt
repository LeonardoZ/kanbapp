package com.github.leonardoz.kanbapp.data.migration

object SqlReference {

    val createColumnTable =
        """CREATE TABLE IF NOT EXISTS `column`
           (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
           `title` TEXT NOT NULL,
           `type` TEXT NOT NULL,
           `board_id` INTEGER NOT NULL,
           `created_at` INTEGER NOT NULL,
           `updated_at` INTEGER NOT NULL,
           FOREIGN KEY(`board_id`) REFERENCES `boards`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )"""
            .trimMargin()

    val addFieldOrderToColumn = """
        ALTER TABLE column
        ADD COLUMN `order` INTEGER NOT NULL DEFAULT 1;
    """.trimIndent()

    val changeColumnTableName = """
        ALTER TABLE column RENAME to columns;
    """.trimIndent()

    val addSearchIndexToColumnTable = """
       CREATE INDEX `idx_board_id_order` ON columns (`board_id`, `order`)
    """.trimIndent()
}