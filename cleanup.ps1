Write-Host "Iniciando limpieza de archivos legacy..."

# Lista de archivos y carpetas a eliminar
$pathsToRemove = @(
    "pom.xml",
    "src",
    "data",
    "frontend/_legacy_flat_files",
    "remove_legacy_files.bat"
)

foreach ($path in $pathsToRemove) {
    if (Test-Path $path) {
        try {
            Remove-Item $path -Recurse -Force -ErrorAction Stop
            Write-Host "Eliminado: $path" -ForegroundColor Green
        } catch {
            Write-Host "Error al eliminar $path : $_" -ForegroundColor Red
        }
    } else {
        Write-Host "No encontrado (ya eliminado): $path" -ForegroundColor Gray
    }
}

Write-Host "Limpieza completada." -ForegroundColor Cyan
Write-Host "Ahora puedes ejecutar 'npm install' en la raíz y en la carpeta 'frontend'."
