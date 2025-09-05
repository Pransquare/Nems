package com.pransquare.nems.models;

public class PerformanceParameterModel {

	private String performanceParameter;
    private String comments;
    private String selfRating;
    private String managerRating;
    private String managerComments;
    private String finalRating;
    private String finalComments;

    // Constructor, Getters, and Setters
   

    public String getPerformanceParameter() {
        return performanceParameter;
    }

    public PerformanceParameterModel(String performanceParameter, String comments, String selfRating,
			String managerRating, String managerComments, String finalRating, String finalComments) {
		super();
		this.performanceParameter = performanceParameter;
		this.comments = comments;
		this.selfRating = selfRating;
		this.managerRating = managerRating;
		this.managerComments = managerComments;
		this.finalRating = finalRating;
		this.finalComments = finalComments;
	}

	public String getComments() {
        return comments;
    }

    public String getSelfRating() {
        return selfRating;
    }

    public String getManagerRating() {
        return managerRating;
    }

    public String getManagerComments() {
        return managerComments;
    }

	public String getFinalRating() {
		return finalRating;
	}

	public void setFinalRating(String finalRating) {
		this.finalRating = finalRating;
	}

	public String getFinalComments() {
		return finalComments;
	}

	public void setFinalComments(String finalComments) {
		this.finalComments = finalComments;
	}

	public void setPerformanceParameter(String performanceParameter) {
		this.performanceParameter = performanceParameter;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public void setSelfRating(String selfRating) {
		this.selfRating = selfRating;
	}

	public void setManagerRating(String managerRating) {
		this.managerRating = managerRating;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}
    
    
}
