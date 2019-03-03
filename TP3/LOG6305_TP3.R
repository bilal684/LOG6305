install.packages("DAAG")
library(DAAG)


##Calculate correlation of all features (ignore the bugs column)
data_path <- "."

train_file <- "qt50.csv"

train_data <- read.csv(train_file, header=T, sep=",")

features_cols <- c("size", "complexity", "change_churn", "total", "minor", "major", 
                   "ownership", "review_rate", "review_churn_rate", "self_reviews", 
                   "too_quick", "little_discussion", "bugs")

train_data <- train_data[, features_cols]

res <- cor(train_data, method="spearman")

round(res, 3)

