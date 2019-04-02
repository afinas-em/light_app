package tech.bottleneck.the_lightapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class QuizModel implements Parcelable {

    @SerializedName("option-B")
    private String optionB;

    @SerializedName("option-C")
    private String optionC;

    @SerializedName("option-A")
    private String optionA;

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;

    @SerializedName("option-D")
    private String optionD;

    @SerializedName("module")
    private String module;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("category")
    private String category;

    @SerializedName("exam_name")
    private String examName;

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    QuizModel(){

    }

    protected QuizModel(Parcel in) {
        optionB = in.readString();
        optionC = in.readString();
        optionA = in.readString();
        question = in.readString();
        answer = in.readString();
        optionD = in.readString();
        module = in.readString();
        name = in.readString();
        id = in.readString();
        category = in.readString();
        examName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(optionB);
        dest.writeString(optionC);
        dest.writeString(optionA);
        dest.writeString(question);
        dest.writeString(answer);
        dest.writeString(optionD);
        dest.writeString(module);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(category);
        dest.writeString(examName);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QuizModel> CREATOR = new Parcelable.Creator<QuizModel>() {
        @Override
        public QuizModel createFromParcel(Parcel in) {
            return new QuizModel(in);
        }

        @Override
        public QuizModel[] newArray(int size) {
            return new QuizModel[size];
        }
    };
}