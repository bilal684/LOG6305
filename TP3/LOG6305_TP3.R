install.packages("DAAG")
library(DAAG)


############Calculate correlation of all features (ignore the bugs column)############
#Set the directory data path to the current working directory.
data_path <- "."

#Define the training data file
train_file <- "qt50.csv"
#Loading the data from the training file.
train_data <- read.csv(train_file, header=T, sep=",")
#Transform the bugs data column into binary, if there are > 0 bug, then 1, else 0.
train_data$bugs <- train_data$bugs > 0
#Define the columns to retain, e.g will let us remove the "comp" column which is non numerical
cols <- c("size", "complexity", "change_churn", "total", "minor", "major", 
                   "ownership", "review_rate", "review_churn_rate", "self_reviews", 
                   "too_quick", "little_discussion", "bugs")
#Will only keep the numerical data and remove the "comp" column
train_data <- train_data[, cols]
#Calculate the spearman corelation matrix.
res <- cor(train_data, method="spearman")
#Round the corelation data to 3 decimal digits (more convenient to analyze)
round(res, 3)


############Building the first model, bugs ~ size + review_rate + review_churn_rate + too_quick, since very corelated##########
#Create the model formula, bugs ~ size + review_rate + review_churn_rate + too_quick because these features are very corelated.
m1_formula <- as.formula("bugs ~ size + review_rate + review_churn_rate + too_quick")
#Build the logit model for binary data, e.g to determine the presence or not of bugs in the source files
m1_model <- glm(m1_formula, data=train_data, family=binomial("logit"))
#To display the model built-in summary to display the statistical test
summary(m1_model)
#To analyze the contribution of each variable using ANOVA
anova(m1_model, test="Chisq")
#This is used to perform the drop one tests to measure the impact of each explanatory variable on AIC value.
drop1(m1_model, test="Chisq")
#This is to display the AIC value of the model
AIC(m1_model)



#Create the model formula, bugs ~ complexity + rewview_rate + review_churn_rate + too_quick because these features are very corelated.
m2_formula <- as.formula("bugs ~ size : complexity + minor + major + ownership + review_rate")
#Build the logit model for binary data, e.g to determine the presence or not of bugs in the source files
m2_model <- glm(m2_formula, data=train_data, family=binomial("logit"))
#To display the model built-in summary to display the statistical test
summary(m2_model)
#To analyze the contribution of each variable using ANOVA
anova(m2_model, test="Chisq")
#This is used to perform the drop one tests to measure the impact of each explanatory variable on AIC value.
drop1(m2_model, test="Chisq")
#This is to display the AIC value of the model
AIC(m2_model)

##########Testing the chosen m1_model(m1_model instead of XXXXXXXXXX model because YYYYYYYYY)##########
test_file <- "qt51.csv"

test_data <- read.csv(test_file, header=T, sep=",")

test_data$bugs <- test_data$bugs > 0#Transform bugs column into binary column (if > 0 -> 1, else 0)

outcome_pred_m1_model <- predict(m1_model, type="response", newdata = test_data)

confusion_matrix_m1_model <- table(outcome_pred_m1_model > 0.5, test_data$bugs)

print(confusion_matrix_m1_model)

accuracy_m1_model <- (confusion_matrix_m1_model["FALSE", "FALSE"] + confusion_matrix_m1_model["TRUE", "TRUE"]) / sum(confusion_matrix_m1_model)

print(accuracy_m1_model)

outcome_pred_m2_model <- predict(m2_model, type="response", newdata = test_data)

confusion_matrix_m2_model <- table(outcome_pred_m2_model > 0.5, test_data$bugs)

print(confusion_matrix_m2_model)

accuracy_m2_model <- (confusion_matrix_m2_model["FALSE", "FALSE"] + confusion_matrix_m2_model["TRUE", "TRUE"]) / sum(confusion_matrix_m2_model)

print(accuracy_m2_model)
