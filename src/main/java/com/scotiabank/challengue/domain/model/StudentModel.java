package com.scotiabank.challengue.domain.model;

public record StudentModel(
        Long id,
        String name,
        String lastName,
        String status,
        Integer age
) {

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .name(this.name)
                .lastName(this.lastName)
                .status(this.status)
                .age(this.age);
    }

    public static final class Builder {
        private Long id;
        private String name;
        private String lastName;
        private String status;
        private Integer age;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public StudentModel build() {
            return new StudentModel(id, name, lastName, status, age);
        }
    }
}

