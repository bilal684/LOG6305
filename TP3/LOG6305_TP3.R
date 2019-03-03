install.packages("DAAG")
library(DAAG)


##Calculate correlation of all features (ignore the bugs column)
data_path <- "."

train_file <- "qt50.csv"

train_data <- read.csv(train_file, header=T, sep=",")

train_data$bugs <- as.integer(train_data$bugs > 0)#Transform bugs column into binary column (if > 0 -> 1, else 0)

features_cols <- c("size", "complexity", "change_churn", "total", "minor", "major", 
                   "ownership", "review_rate", "review_churn_rate", "self_reviews", 
                   "too_quick", "little_discussion", "bugs")

train_data <- train_data[, features_cols]

res <- cor(train_data, method="spearman")

round(res, 3)

m1_formula <- as.formula("bugs ~ size + review_rate + review_churn_rate + too_quick")

m1_model <- glm(m1_formula, data=train_data, family=binomial("logit"))

summary(m1_model)

anova(m1_model, test="Chisq")

drop1(m1_model, test="Chisq")

AIC(m1_model)

test_file <- "qt51.csv"

test_data <- read.csv(test_file, header=T, sep=",")

test_data$bugs <- as.integer(test_data$bugs > 0)#Transform bugs column into binary column (if > 0 -> 1, else 0)

outcome_pred <- predict(m1_model, type="response", newdata = test_data)

confusion_matrix <- table(outcome_pred > 0.5, test_data$bugs)

accuracy <- (confusion_matrix["FALSE", "0"] + confusion_matrix["TRUE", "1"] / sum(confusion_matrix)

print(accuracy)
