#version 130

in vec3 position;
in vec3 normal;

uniform mat4 viewProjectionMatrix;

uniform vec3 lightPos;

out vec3 surfaceNormal;
out vec3 toLightVec;

void main(){
    vec4 worldPos = vec4(position,1);
    gl_Position = viewProjectionMatrix * worldPos;
    
    surfaceNormal = normal;
    toLightVec = lightPos - worldPos.xyz;
}
