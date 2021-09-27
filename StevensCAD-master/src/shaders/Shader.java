/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shaders;

import editorV2.Light;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL20.*;

/**
 *
 * @author itay
 */
public class Shader {

    public Shader() {
    }

    public void bind(int program) {
        glUseProgram(program);
    }

    public int loadShader(String filenameAndExtention, int type) { //Returns Shader ID
        int shader = glCreateShader(type);

        //Set source
        glShaderSource(shader, loadFile(filenameAndExtention));

        //Compile Shader
        glCompileShader(shader);

        //Check if shader compiled
        if (glGetShaderi(shader, GL_COMPILE_STATUS) != 1) {
            System.out.println("Can't compile shaders");
            System.out.println(glGetShaderInfoLog(shader));
            return -1;
        }

        //return the ID 
        return shader;
    }

    public int loadShaders(String vertexShader, String fragmentShader) { //Returns programID

        //load up shaders
        int vsID = loadShader(vertexShader + ".vs", GL_VERTEX_SHADER);
        int fsID = loadShader(fragmentShader + ".fs", GL_FRAGMENT_SHADER);

        //Create Program
        int program = glCreateProgram();

        //Attach the Shaders to program
        glAttachShader(program, vsID);
        glAttachShader(program, fsID);

        //Link Program
        glLinkProgram(program);

        //Check Link
        if (glGetProgrami(program, GL_LINK_STATUS) != 1) {
            System.out.println("Can't Link Program!");
            return -1;
        }

        //Validate Program
        glValidateProgram(program);

        //Check Validation
        if (glGetProgrami(program, GL_VALIDATE_STATUS) != 1) {
            System.out.println("Can't Validate Program!");
            return -1;
        }

        return program;
    }

    private String loadFile(String filename) {
        StringBuilder s = new StringBuilder();
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(new File("src/shaders/" + filename)));
            String line;

            while ((line = br.readLine()) != null) {
                s.append(line);
                s.append("\n");
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    public void loadTexture(String filename) {
        //TODO
    }

    public void setUniform(int program, String name, Matrix4f val) {
        int location = glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16); //Size of 4f matrix
        val.get(buffer);

        if (location != -1) {
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void setUniform(int program, String name, Vector4f val) {
        int location = glGetUniformLocation(program, name);

        if (location != -1) {
            glUniform4f(location, val.x, val.y, val.z, val.w);
        }
    }

    public void setUniform(int program, String name, Vector3f val) {
        int location = glGetUniformLocation(program, name);

        if (location != -1) {
            glUniform3f(location, val.x, val.y, val.z);
        }
    }

    public void loadLight(int program, Light light) {
        setUniform(program, "lightColor", light.getColor());
        setUniform(program, "lightPos", light.getPosition());
    }

    public void bindAttributes(int program, int attribute, String name) {
        glBindAttribLocation(program, attribute, name);
    }

}
