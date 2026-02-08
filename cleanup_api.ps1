Write-Host "Limpiando archivos de API duplicados..."

$apiRoot = "api"

$filesToRemove = @(
    "$apiRoot/db.ts",
    "$apiRoot/getEnvVarOrThrow.ts",
    "$apiRoot/AuthTokenManagement.ts",
    "$apiRoot/jsonResponseUtils.ts",
    "$apiRoot/jsonResponseHandlers.ts",
    "$apiRoot/GetPassDetailsHandler.ts",
    "$apiRoot/AdminHandlers.ts"
)

$dirsToRemove = @(
    "$apiRoot/auth",
    "$apiRoot/admin",
    "$apiRoot/passes"
)

foreach ($file in $filesToRemove) {
    if (Test-Path $file) {
        Remove-Item $file -Force
        Write-Host "Eliminado archivo: $file" -ForegroundColor Green
    }
}

foreach ($dir in $dirsToRemove) {
    if (Test-Path $dir) {
        Remove-Item $dir -Recurse -Force
        Write-Host "Eliminada carpeta: $dir" -ForegroundColor Green
    }
}

Write-Host "Limpieza de API completada." -ForegroundColor Cyan
